package com.eat.today.sns.command.domain.service;

import com.eat.today.sns.command.application.entity.message.DmFileUploadEntity;
import com.eat.today.sns.command.application.entity.message.NoteMessageEntity;
import com.eat.today.sns.command.domain.repository.message.NoteFileUploadRepo;
import com.eat.today.sns.command.domain.repository.message.NoteMessageRepo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class NoteCommandService {
    private final NoteMessageRepo noteRepo;
    private final NoteFileUploadRepo fileRepo;

    @Value("${app.upload.base-dir}")
    private String uploadBaseDir;              // 예: ${user.home}/yls/EatToday_upload

    private Path baseDir;                      // 정규화된 절대 경로 캐시

    private static final DateTimeFormatter ISO_Z =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneId.of("UTC"));
    private String nowIso(){ return ISO_Z.format(Instant.now()); }

    /** 앱 기동 시 업로드 베이스 경로 점검/생성 */
    @PostConstruct
    void ensureBaseDir() {
        try {
            baseDir = Paths.get(uploadBaseDir).toAbsolutePath().normalize();
            Files.createDirectories(baseDir);
            if (!Files.isWritable(baseDir)) {
                throw new IllegalStateException("Upload base-dir is not writable: " + baseDir);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Invalid app.upload.base-dir: " + uploadBaseDir, e);
        }
    }

    // 1) 전송 (텍스트 전용)
    public NoteMessageEntity send(Integer sender, Integer receiver, String subject, String content) {
        var n = new NoteMessageEntity();
        n.setSenderNo(sender); n.setReceiverNo(receiver);
        n.setSubject(subject); n.setContent(content);
        n.setSentAtTxt(nowIso());
        return noteRepo.save(n);
    }

    // 1-β) 전송 (멀티파트: 이미지/파일 포함)
    public NoteMessageEntity sendWithFiles(Integer sender, Integer receiver, String subject, String content,
                                           List<MultipartFile> files) {
        var saved = send(sender, receiver, subject, content);
        saveFiles(saved.getNoteId(), files);
        return saved;
    }

    // 2) 읽음 처리(수신자만)
    public void markRead(Integer noteId, Integer readerNo) {
        var n = noteRepo.findById(noteId).orElseThrow();
        if (!n.getReceiverNo().equals(readerNo)) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        if (!n.isRead()) { n.setRead(true); n.setReadAtTxt(nowIso()); noteRepo.save(n); }
    }

    // 3) 답장 = “새 쪽지” 생성 (스레드 연결)
    public NoteMessageEntity reply(Integer noteId, Integer senderNo, String content) {
        var orig = noteRepo.findById(noteId).orElseThrow();
        if (!Objects.equals(senderNo, orig.getSenderNo()) && !Objects.equals(senderNo, orig.getReceiverNo()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        var replied = new NoteMessageEntity();
        replied.setSenderNo(senderNo);
        replied.setReceiverNo(Objects.equals(senderNo, orig.getSenderNo()) ? orig.getReceiverNo() : orig.getSenderNo());
        replied.setSubject("RE: " + Optional.ofNullable(orig.getSubject()).orElse("(no subject)"));
        replied.setContent(content);
        replied.setSentAtTxt(nowIso());
        replied.setReplyToId(orig.getNoteId());
        return noteRepo.save(replied);
    }

    // 4) 삭제(개인함에서만): 발신자/수신자 플래그 각각
    public void deleteForUser(Integer noteId, Integer actorNo) {
        var n = noteRepo.findById(noteId).orElseThrow();
        if (Objects.equals(actorNo, n.getSenderNo())) { n.setSenderDeleted(true); noteRepo.save(n); return; }
        if (Objects.equals(actorNo, n.getReceiverNo())) { n.setReceiverDeleted(true); noteRepo.save(n); return; }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    // 저장 디렉터리: {base}/notes/{noteId}
    private Path noteDir(Integer noteId) {
        return baseDir.resolve("notes").resolve(String.valueOf(noteId));
    }

    // 안전한 파일명 생성
    private static String safeStoredName(String original) {
        String cleaned = StringUtils.cleanPath(Optional.ofNullable(original).orElse("file"));
        if (cleaned.contains("..")) throw new IllegalArgumentException("Invalid path sequence in filename: " + cleaned);
        String ext = "";
        int dot = cleaned.lastIndexOf('.');
        if (dot != -1) ext = cleaned.substring(dot);
        return UUID.randomUUID().toString().replace("-", "") + ext;
    }

    // 5) 파일 저장(로컬) — 절대 경로 + 상위 폴더 보장 + Files.copy 사용
    private void saveFiles(Integer noteId, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) return;

        Path dir = noteDir(noteId);
        try {
            Files.createDirectories(dir); // 상위 디렉터리 보장
        } catch (IOException e) {
            throw new RuntimeException("Failed to create upload directory: " + dir, e);
        }

        for (MultipartFile mf : files) {
            if (mf == null || mf.isEmpty()) continue;

            // 크기 제한 (10MB)
            if (mf.getSize() > 10L * 1024 * 1024) {
                throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "File too large (max 10MB)");
            }

            String originalName = Optional.ofNullable(mf.getOriginalFilename()).orElse("file");
            String storedName = safeStoredName(originalName);
            Path target = dir.resolve(storedName);

            try (var in = mf.getInputStream()) {
                // transferTo 대신 Files.copy 사용 (임시 파일/이동 이슈 회피)
                Files.copy(in, target);
            } catch (IOException e) {
                throw new RuntimeException("File save failed to " + target + " : " + e.getMessage(), e);
            }

            // 공개 URL (정적 리소스 핸들러: /files/** → file:${app.upload.base-dir}/)
            String publicUrl = "/notes/" + noteId + "/" + storedName;

            var fe = new DmFileUploadEntity();
            fe.setNoteId(noteId);
            fe.setDmFileName(originalName);
            fe.setDmFileType(Optional.ofNullable(mf.getContentType()).orElse("application/octet-stream"));
            fe.setDmFileRename(storedName);
            fe.setDmFilePath(target.toString().replace('\\','/')); // 내부 물리 경로(기존 컬럼 유지)
            fe.setDmFileAt(nowIso());
            fe.setDmFileSize(mf.getSize());

            // (선택) dm_file_url 컬럼이 있으면 주석 해제
            // fe.setDmFileUrl(publicUrl);

            fileRepo.save(fe);
        }
    }
}
