package com.eat.today.sns.command.domain.service;

import com.eat.today.member.command.application.service.MemberPointService;
import com.eat.today.member.command.domain.aggregate.PointPolicy;
import com.eat.today.sns.command.application.dto.photoReviewDTO.CreateRequest;
import com.eat.today.sns.command.application.dto.photoReviewDTO.UpdateRequest;
import com.eat.today.sns.command.application.entity.photoReview.PhotoReviewEntity;
import com.eat.today.sns.command.application.entity.prFileUpload.PrFileUploadEntity;
import com.eat.today.sns.command.domain.repository.photoReview.PhotoReviewRepository;
import com.eat.today.sns.command.domain.repository.prFileUpload.PrFileUploadRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PhotoReviewCommandService {

    private final PhotoReviewRepository repository;
    private final PrFileUploadRepository fileRepo;
    private final MemberPointService memberPointService;

    @Value("${app.upload.base-dir}")
    private String uploadBaseDir;
    private Path baseDir;
    private static final long MAX_SIZE = 10L * 1024 * 1024;   // 10MB

    private static final Set<String> ALLOWED_CT = Set.of(
            "image/jpeg", "image/png", "image/gif", "image/webp", "image/heic", "image/heif"
    );

    private static final DateTimeFormatter ISO_Z =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneId.of("UTC"));

    private String nowIso() { return ISO_Z.format(Instant.now()); }

    @PostConstruct
    void ensureBaseDir() {
        try {
            baseDir = Paths.get(uploadBaseDir).toAbsolutePath().normalize();
            Files.createDirectories(baseDir);
            if (!Files.isWritable(baseDir)) {
                throw new IllegalStateException("Upload base-dir not writable: " + baseDir);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Invalid app.upload.base-dir: " + uploadBaseDir, e);
        }
    }

    // 경로 이름
    private Path reviewDir(int reviewNo) {
        return baseDir.resolve("reviews").resolve(String.valueOf(reviewNo));
    }

    private static String safeStoredName(String original) {
        String cleaned = StringUtils.cleanPath(Objects.requireNonNullElse(original, "file"));
        if (cleaned.contains("..")) {
            throw new IllegalArgumentException("Invalid filename: " + cleaned);
        }
        String ext = "";
        int dot = cleaned.lastIndexOf('.');
        if (dot != -1) ext = cleaned.substring(dot);
        return UUID.randomUUID().toString().replace("-", "") + ext;
    }

    // 사진 리뷰 삽입
    public int create(CreateRequest req) {
        PhotoReviewEntity e = new PhotoReviewEntity();
        e.setBoardNo(req.getBoardNo());
        e.setMemberNo(req.getMemberNo());
        e.setReviewTitle(req.getReviewTitle());
        e.setReviewDate(req.getReviewDate());
        e.setReviewContent(req.getReviewContent());
        e.setReviewLike(0);
        PhotoReviewEntity saved = repository.save(e);

        grantPointSafe(req.getMemberNo(), saved.getReviewNo());
        return saved.getReviewNo();
    }

    // 사진리뷰 삽입
    public int create(CreateRequest req, List<MultipartFile> files) {
        PhotoReviewEntity e = new PhotoReviewEntity();
        e.setBoardNo(req.getBoardNo());
        e.setMemberNo(req.getMemberNo());
        e.setReviewTitle(req.getReviewTitle());
        e.setReviewDate(req.getReviewDate());
        e.setReviewContent(req.getReviewContent());
        e.setReviewLike(0);
        e = repository.save(e);

        saveFiles(e.getReviewNo(), files);

        grantPointSafe(req.getMemberNo(), e.getReviewNo());
        return e.getReviewNo();
    }

    // 글 + 파일 수정 / 삭제
    public int editWithFiles(int reviewNo,
                             UpdateRequest reqPatch,
                             List<MultipartFile> addFiles,
                             List<Integer> deleteFileNos) {

        PhotoReviewEntity e = repository.findById(reviewNo)
                .orElseThrow(() -> new EntityNotFoundException("review_no=" + reviewNo));

        // 본문 패치
        if (reqPatch != null) {
            if (reqPatch.getBoardNo() != null) e.setBoardNo(reqPatch.getBoardNo());
            if (reqPatch.getMemberNo() != null) e.setMemberNo(reqPatch.getMemberNo());
            if (reqPatch.getReviewTitle() != null) e.setReviewTitle(reqPatch.getReviewTitle());
            if (reqPatch.getReviewDate() != null) e.setReviewDate(reqPatch.getReviewDate());
            if (reqPatch.getReviewContent() != null) e.setReviewContent(reqPatch.getReviewContent());
            if (reqPatch.getReviewLike() != null) e.setReviewLike(reqPatch.getReviewLike());
            repository.save(e);
        }

        // 파일 삭제
        if (deleteFileNos != null && !deleteFileNos.isEmpty()) {
            var files = fileRepo.findAllById(deleteFileNos);
            for (var fe : files) {
                if (!Objects.equals(fe.getReview().getReviewNo(), reviewNo)) continue;
                try { Files.deleteIfExists(Paths.get(fe.getPrFilePath())); } catch (Exception ignore) {}
                fileRepo.delete(fe);
            }
        }

        // 파일 추가
        saveFiles(reviewNo, addFiles);

        return 1;
    }

    // 글 + 파일 삭제
    public int delete(int reviewNo) {
        var eOpt = repository.findById(reviewNo);
        if (eOpt.isEmpty()) return 0;

        var files = fileRepo.findByReview_ReviewNo(reviewNo);
        for (var fe : files) {
            try { Files.deleteIfExists(Paths.get(fe.getPrFilePath())); } catch (Exception ignore) {}
        }

        repository.deleteById(reviewNo); // FK CASCADE면 파일 레코드도 삭제됨
        return 1;
    }

    // 파일만 삭제
    public int deleteFile(int reviewNo, int fileNo) {
        var feOpt = fileRepo.findById(fileNo);
        if (feOpt.isEmpty()) return 0;

        var fe = feOpt.get();
        if (!Objects.equals(fe.getReview().getReviewNo(), reviewNo)) return 0;

        try { Files.deleteIfExists(Paths.get(fe.getPrFilePath())); } catch (Exception ignore) {}
        fileRepo.delete(fe);
        return 1;
    }

    // 파일 저장
    private void saveFiles(int reviewNo, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) return;

        Path dir = reviewDir(reviewNo); // baseDir.resolve("reviews").resolve(reviewNo)
        try { Files.createDirectories(dir); }
        catch (IOException e) { throw new RuntimeException("Fail to create dir: " + dir, e); }

        List<Path> created = new ArrayList<>();
        try {
            for (MultipartFile mf : files) {
                if (mf == null || mf.isEmpty()) continue;

                String originalName = Optional.ofNullable(mf.getOriginalFilename()).orElse("image");
                String storedName = safeStoredName(originalName);
                Path target = dir.resolve(storedName);

                try (var in = mf.getInputStream()) { Files.copy(in, target); }
                created.add(target);

                String physicalPath = target.toString().replace('\\','/');

                // 공개 URL: /files/{relative-to-basedir}
                String relative = baseDir.relativize(target).toString().replace('\\','/');
                String publicUrl = "/" + ("files/" + relative).replaceAll("/{2,}", "/");

                PrFileUploadEntity fe = new PrFileUploadEntity();
                fe.setReview(repository.getReferenceById(reviewNo));
                fe.setPrFileName(originalName);
                fe.setPrFileType(Optional.ofNullable(mf.getContentType()).orElse("application/octet-stream"));
                fe.setPrFileRename(storedName);
                fe.setPrFilePath(physicalPath);
                fe.setPrFileUrl(publicUrl);
                fe.setPrFileAt(nowIso());
                fileRepo.save(fe);
            }
        } catch (RuntimeException | IOException ex) {
            for (Path p : created) { try { Files.deleteIfExists(p); } catch (Exception ignore) {} }
            throw (ex instanceof RuntimeException) ? (RuntimeException) ex : new RuntimeException(ex);
        }
    }

    private void grantPointSafe(Integer memberNo, Integer reviewNo) {
        try {
            memberPointService.grantPoints(memberNo, PointPolicy.PHOTO_REVIEW_CREATE);
        } catch (Exception ex) {
            log.error("사진 리뷰 작성 포인트 지급 실패 - memberNo={}, reviewNo={}", memberNo, reviewNo, ex);
        }
    }
}
