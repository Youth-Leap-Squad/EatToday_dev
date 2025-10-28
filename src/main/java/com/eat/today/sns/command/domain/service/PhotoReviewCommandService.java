package com.eat.today.sns.command.domain.service;

import com.eat.today.member.command.application.service.MemberPointService;
import com.eat.today.member.command.domain.aggregate.PointPolicy;
import com.eat.today.sns.command.application.dto.photoReviewDTO.CreateRequest;
import com.eat.today.sns.command.application.dto.photoReviewDTO.UpdateRequest;
import com.eat.today.sns.command.application.entity.photoReview.PhotoReviewEntity;
import com.eat.today.sns.command.application.entity.prFileUpload.PrFileUploadEntity;
import com.eat.today.sns.command.domain.repository.photoReview.PhotoReviewRepository;
import com.eat.today.sns.command.domain.repository.prFileUpload.PrFileUploadRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.nio.file.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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

    /** 실제 파일 저장 디렉터리 (프론트 public/images/photo_review 또는 백엔드 static 폴백) */
    private Path baseDir;

    private static final long MAX_SIZE = 10L * 1024 * 1024;

    private static final Set<String> ALLOWED_CT = Set.of(
            "image/jpeg", "image/png", "image/gif", "image/webp", "image/heic", "image/heif"
    );

    /** 한국시간으로 DB 저장 */
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter KST_DATETIME =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(KST);

    /** 업로드 기록 시각 ISO-Z(UTC) */
    private static final DateTimeFormatter ISO_Z =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneId.of("UTC"));

    private static String nowIsoUtc() { return ISO_Z.format(Instant.now()); }

    /**
     * 업로드 기본 경로 초기화
     * 우선순위:
     *  1) {ROOT}/eatToday_front/eatToday_front/public/images/photo_review
     *  2) {ROOT}/eatToday_front/public/images/photo_review
     *  3) 백엔드 {projectRoot}/src/main/resources/static/images/photo_review (폴백)
     */
    private void initBaseDir() {
        if (baseDir != null) return;

        Path backendRoot = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();
        List<Path> candidates = new ArrayList<>();

        // ① 이중 폴더 구조
        candidates.add(backendRoot.getParent()
                .resolve("eatToday_front")
                .resolve("eatToday_front")
                .resolve("public")
                .resolve("images")
                .resolve("photo_review"));

        // ② 단일 폴더 구조
        candidates.add(backendRoot.getParent()
                .resolve("eatToday_front")
                .resolve("public")
                .resolve("images")
                .resolve("photo_review"));

        // ③ 백엔드 정적 리소스(폴백) - /images/photo_review/** 를 리소스 핸들러에서 매핑했거나 static 서빙할 때 사용
        candidates.add(backendRoot
                .resolve("src")
                .resolve("main")
                .resolve("resources")
                .resolve("static")
                .resolve("images")
                .resolve("photo_review"));

        IllegalStateException lastError = null;
        for (Path p : candidates) {
            try {
                Path np = p.normalize();
                Files.createDirectories(np);
                if (Files.isWritable(np)) {
                    baseDir = np;
                    log.info("[PhotoReview] upload dir resolved: {}", baseDir);
                    return;
                } else {
                    log.warn("[PhotoReview] not writable: {}", np);
                }
            } catch (Exception e) {
                lastError = new IllegalStateException("Make dir failed: " + p, e);
                log.warn("[PhotoReview] create dir failed: {}", p, e);
            }
        }
        throw (lastError != null) ? lastError :
                new IllegalStateException("No writable upload dir found for images/photo_review");
    }

    private static String safeStoredName(String original) {
        String cleaned = StringUtils.cleanPath(Objects.requireNonNullElse(original, "file"));
        if (cleaned.contains("..")) throw new IllegalArgumentException("Invalid filename: " + cleaned);
        String ext = "";
        int dot = cleaned.lastIndexOf('.');
        if (dot != -1) ext = cleaned.substring(dot);
        return UUID.randomUUID().toString().replace("-", "") + ext;
    }

    /* CREATE (본문만) */
    public int create(CreateRequest req) {
        initBaseDir();

        if (!StringUtils.hasText(req.getReviewDate())) {
            req.setReviewDate(KST_DATETIME.format(ZonedDateTime.now(KST)));
        }
        if (req.getReviewLike() == null) req.setReviewLike(0);

        PhotoReviewEntity e = new PhotoReviewEntity();
        e.setBoardNo(req.getBoardNo());
        e.setMemberNo(req.getMemberNo());
        e.setReviewTitle(req.getReviewTitle());
        e.setReviewDate(req.getReviewDate());
        e.setReviewContent(req.getReviewContent());
        e.setReviewLike(req.getReviewLike());

        PhotoReviewEntity saved = repository.save(e);
        grantPointSafe(req.getMemberNo(), saved.getReviewNo());
        return saved.getReviewNo();
    }

    /* CREATE (본문 + 파일) */
    public int create(CreateRequest req, List<MultipartFile> files) {
        initBaseDir();

        if (!StringUtils.hasText(req.getReviewDate())) {
            req.setReviewDate(KST_DATETIME.format(ZonedDateTime.now(KST)));
        }
        if (req.getReviewLike() == null) req.setReviewLike(0);

        PhotoReviewEntity e = new PhotoReviewEntity();
        e.setBoardNo(req.getBoardNo());
        e.setMemberNo(req.getMemberNo());
        e.setReviewTitle(req.getReviewTitle());
        e.setReviewDate(req.getReviewDate());
        e.setReviewContent(req.getReviewContent());
        e.setReviewLike(req.getReviewLike());
        e = repository.save(e);

        saveFiles(e.getReviewNo(), files);
        grantPointSafe(req.getMemberNo(), e.getReviewNo());
        return e.getReviewNo();
    }

    /* UPDATE (본문 + 파일 추가/삭제) */
    public int editWithFiles(int reviewNo,
                             UpdateRequest reqPatch,
                             List<MultipartFile> addFiles,
                             List<Integer> deleteFileNos) {

        initBaseDir();

        PhotoReviewEntity e = repository.findById(reviewNo)
                .orElseThrow(() -> new EntityNotFoundException("review_no=" + reviewNo));

        if (reqPatch != null) {
            if (reqPatch.getBoardNo() != null)       e.setBoardNo(reqPatch.getBoardNo());
            if (reqPatch.getMemberNo() != null)      e.setMemberNo(reqPatch.getMemberNo());
            if (reqPatch.getReviewTitle() != null)   e.setReviewTitle(reqPatch.getReviewTitle());
            if (reqPatch.getReviewDate() != null)    e.setReviewDate(reqPatch.getReviewDate());
            if (reqPatch.getReviewContent() != null) e.setReviewContent(reqPatch.getReviewContent());
            if (reqPatch.getReviewLike() != null)    e.setReviewLike(reqPatch.getReviewLike());
            repository.save(e);
        }

        if (deleteFileNos != null && !deleteFileNos.isEmpty()) {
            var files = fileRepo.findAllById(deleteFileNos);
            for (var fe : files) {
                if (!Objects.equals(fe.getReview().getReviewNo(), reviewNo)) continue;
                try { Files.deleteIfExists(Paths.get(fe.getPrFilePath())); } catch (Exception ignore) {}
                fileRepo.delete(fe);
            }
        }

        saveFiles(reviewNo, addFiles);

        return 1;
    }

    /* DELETE (리뷰 + 파일) */
    public int delete(int reviewNo) {
        initBaseDir();

        var eOpt = repository.findById(reviewNo);
        if (eOpt.isEmpty()) return 0;

        var files = fileRepo.findByReview_ReviewNo(reviewNo);
        for (var fe : files) {
            try { Files.deleteIfExists(Paths.get(fe.getPrFilePath())); } catch (Exception ignore) {}
        }

        repository.deleteById(reviewNo);
        return 1;
    }

    /* 파일만 삭제 */
    public int deleteFile(int reviewNo, int fileNo) {
        initBaseDir();

        var feOpt = fileRepo.findById(fileNo);
        if (feOpt.isEmpty()) return 0;

        var fe = feOpt.get();
        if (!Objects.equals(fe.getReview().getReviewNo(), reviewNo)) return 0;

        try { Files.deleteIfExists(Paths.get(fe.getPrFilePath())); } catch (Exception ignore) {}
        fileRepo.delete(fe);
        return 1;
    }

    /**
     * 파일 저장
     *  - pr_file_path : 물리 경로(디버깅용)
     *  - pr_file_url  : "/images/photo_review/{storedName}"  ← 프론트에서 그대로 <img :src> 사용
     */
    private void saveFiles(int reviewNo, List<MultipartFile> files) {
        initBaseDir();
        if (files == null || files.isEmpty()) return;

        List<Path> created = new ArrayList<>();
        try {
            for (MultipartFile mf : files) {
                if (mf == null || mf.isEmpty()) continue;

                if (mf.getSize() > MAX_SIZE) {
                    throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "파일이 너무 큽니다(최대 10MB)");
                }
                String ct = Optional.ofNullable(mf.getContentType()).orElse("");
                if (!ct.isBlank() && !ALLOWED_CT.contains(ct)) {
                    throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "허용되지 않는 이미지 형식");
                }

                String originalName = Optional.ofNullable(mf.getOriginalFilename()).orElse("image");
                String storedName = safeStoredName(originalName);
                Path target = baseDir.resolve(storedName);

                try (InputStream in = mf.getInputStream()) {
                    Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
                }
                created.add(target);

                String physicalPath = target.toString().replace('\\','/');
                String publicUrl   = "/images/photo_review/" + storedName;

                PrFileUploadEntity fe = new PrFileUploadEntity();
                fe.setReview(repository.getReferenceById(reviewNo));
                fe.setPrFileName(originalName);
                fe.setPrFileType(ct.isBlank() ? "application/octet-stream" : ct);
                fe.setPrFileRename(storedName);
                fe.setPrFilePath(physicalPath);
                fe.setPrFileUrl(publicUrl);      // ✅ 프론트가 바로 쓸 URL 저장
                fe.setPrFileAt(nowIsoUtc());
                fileRepo.save(fe);

                log.debug("[PhotoReview] saved file: {}\n  url={}\n  path={}", originalName, publicUrl, physicalPath);
            }
        } catch (RuntimeException | java.io.IOException ex) {
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