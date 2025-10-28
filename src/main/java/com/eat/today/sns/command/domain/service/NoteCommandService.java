package com.eat.today.sns.command.domain.service;

import com.eat.today.sns.command.application.entity.message.DmFileUploadEntity;
import com.eat.today.sns.command.application.entity.message.NoteMessageEntity;
import com.eat.today.sns.command.domain.repository.message.NoteFileUploadRepo;
import com.eat.today.sns.command.domain.repository.message.NoteMessageRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * ✅ yml 없이도 동작하는 쪽지(NOTE) 서비스
 * 파일은 자동으로 eatToday_front/public/files/notes/{noteId}/ 에 저장됨
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NoteCommandService {

    private final NoteMessageRepo noteRepo;
    private final NoteFileUploadRepo fileRepo;

    /** ✅ 기본 저장 위치 캐시 */
    private Path baseDir;

    private static final DateTimeFormatter ISO_Z =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneId.of("UTC"));
    private String nowIso(){ return ISO_Z.format(Instant.now()); }

    /** ✅ yml 없이도 동작하는 baseDir 자동 설정 */
    private void initBaseDir() {
        if (baseDir != null) return;

        try {
            // 프로젝트 루트 기준으로 프론트 public/files/notes 폴더 자동 계산
            Path projectRoot = Paths.get(System.getProperty("user.dir")).toAbsolutePath();
            Path frontDir = projectRoot.resolveSibling("eatToday_front/public/files/notes");

            baseDir = frontDir.normalize();
            Files.createDirectories(baseDir);
            if (!Files.isWritable(baseDir)) {
                throw new IllegalStateException("쪽지 파일 업로드 폴더에 쓰기 권한이 없습니다: " + baseDir);
            }

            log.info("[NoteCommandService] upload dir resolved: {}", baseDir);
        } catch (Exception e) {
            throw new IllegalStateException("쪽지 업로드 경로 초기화 실패", e);
        }
    }

    // ✅ 안전한 파일명 (UUID)
    private static String safeStoredName(String original) {
        String cleaned = StringUtils.cleanPath(Optional.ofNullable(original).orElse("file"));
        if (cleaned.contains("..")) throw new IllegalArgumentException("Invalid filename: " + cleaned);
        String ext = "";
        int dot = cleaned.lastIndexOf('.');
        if (dot != -1) ext = cleaned.substring(dot);
        return UUID.randomUUID().toString().replace("-", "") + ext;
    }

    // =====================================================
    // 1) 쪽지 전송 (텍스트 전용)
    // =====================================================
    public NoteMessageEntity send(Integer sender, Integer receiver, String subject, String content) {
        var n = new NoteMessageEntity();
        n.setSenderNo(sender);
        n.setReceiverNo(receiver);
        n.setSubject(subject);
        n.setContent(content);
        n.setSentAtTxt(nowIso());
        return noteRepo.save(n);
    }

    // =====================================================
    // 2) 쪽지 전송 (파일 포함)
    // =====================================================
    public NoteMessageEntity sendWithFiles(Integer sender, Integer receiver, String subject, String content,
                                           List<MultipartFile> files) {
        var saved = send(sender, receiver, subject, content);
        saveFiles(saved.getNoteId(), files);
        return saved;
    }

    // =====================================================
    // 3) 읽음 처리
    // =====================================================
    public void markRead(Integer noteId, Integer readerNo) {
        var n = noteRepo.findById(noteId).orElseThrow();
        if (!n.getReceiverNo().equals(readerNo))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "읽기 권한이 없습니다.");
        if (!n.isRead()) {
            n.setRead(true);
            n.setReadAtTxt(nowIso());
            noteRepo.save(n);
        }
    }

    // =====================================================
    // 4) 답장
    // =====================================================
    public NoteMessageEntity reply(Integer noteId, Integer senderNo, String content) {
        var orig = noteRepo.findById(noteId).orElseThrow();
        if (!Objects.equals(senderNo, orig.getSenderNo()) &&
                !Objects.equals(senderNo, orig.getReceiverNo()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "답장 권한이 없습니다.");

        var replied = new NoteMessageEntity();
        replied.setSenderNo(senderNo);
        replied.setReceiverNo(Objects.equals(senderNo, orig.getSenderNo())
                ? orig.getReceiverNo()
                : orig.getSenderNo());
        replied.setSubject("RE: " + Optional.ofNullable(orig.getSubject()).orElse("(no subject)"));
        replied.setContent(content);
        replied.setSentAtTxt(nowIso());
        replied.setReplyToId(orig.getNoteId());
        return noteRepo.save(replied);
    }

    // =====================================================
    // 5) 삭제 (각자 개인함에서)
    // =====================================================
    public void deleteForUser(Integer noteId, Integer actorNo) {
        var n = noteRepo.findById(noteId).orElseThrow();
        if (Objects.equals(actorNo, n.getSenderNo())) {
            n.setSenderDeleted(true);
            noteRepo.save(n);
            return;
        }
        if (Objects.equals(actorNo, n.getReceiverNo())) {
            n.setReceiverDeleted(true);
            noteRepo.save(n);
            return;
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다.");
    }

    // =====================================================
    // 6) 파일 저장 (프론트 public/files/notes)
    // =====================================================
    private void saveFiles(Integer noteId, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) return;
        initBaseDir();

        Path dir = baseDir.resolve(String.valueOf(noteId));
        try { Files.createDirectories(dir); }
        catch (IOException e) { throw new RuntimeException("폴더 생성 실패: " + dir, e); }

        for (MultipartFile mf : files) {
            if (mf == null || mf.isEmpty()) continue;

            if (mf.getSize() > 10L * 1024 * 1024)
                throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "파일이 너무 큽니다 (10MB 이하만 가능)");

            String originalName = Optional.ofNullable(mf.getOriginalFilename()).orElse("file");
            String storedName = safeStoredName(originalName);
            Path target = dir.resolve(storedName);

            try (var in = mf.getInputStream()) {
                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException("파일 저장 실패: " + target, e);
            }

            // ✅ 프론트 접근 경로 (public 폴더 기준)
            String publicUrl = "/files/notes/" + noteId + "/" + storedName;

            var fe = new DmFileUploadEntity();
            fe.setNoteId(noteId);
            fe.setDmFileName(originalName);
            fe.setDmFileType(Optional.ofNullable(mf.getContentType()).orElse("application/octet-stream"));
            fe.setDmFileRename(storedName);
            fe.setDmFilePath(target.toString().replace('\\', '/'));
            fe.setDmFileAt(nowIso());
            fe.setDmFileSize(mf.getSize());
            // dm_file_url 컬럼이 있다면 추가
            // fe.setDmFileUrl(publicUrl);

            fileRepo.save(fe);
        }
    }
}