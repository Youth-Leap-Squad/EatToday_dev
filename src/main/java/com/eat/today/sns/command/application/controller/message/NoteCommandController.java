package com.eat.today.sns.command.application.controller.message;

import com.eat.today.sns.command.application.entity.message.NoteMessageEntity;
import com.eat.today.sns.command.domain.repository.message.NoteMessageRepo;
import com.eat.today.sns.command.domain.service.NoteCommandService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173"}, allowCredentials = "true")
public class NoteCommandController {

    private static final Logger log = LoggerFactory.getLogger(NoteCommandController.class);

    private final NoteCommandService svc;
    private final NoteMessageRepo noteRepo;

    /* ------------------------------
     *  공통: 인증에서 memberNo 꺼내기
     * ------------------------------ */
    private Integer extractMemberNo(Authentication auth){
        if (auth == null || !auth.isAuthenticated()) return null;
        try {
            Object principal = auth.getPrincipal();
            if (principal != null) {
                try {
                    var m = principal.getClass().getMethod("getMemberNo");
                    Object v = m.invoke(principal);
                    if (v != null) return Integer.valueOf(v.toString());
                } catch (NoSuchMethodException ignored) {}
            }
            try {
                return Integer.valueOf(auth.getName());
            } catch (Exception ignored) {}
        } catch (Exception e){
            log.warn("[note] memberNo 추출 실패: {}", e.getMessage());
        }
        return null;
    }

    /* ============ 보내기 ============ */

    // 보냄(텍스트 JSON)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendJson(@RequestBody Map<String,Object> body){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer senderNo = extractMemberNo(auth);
        if (senderNo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","unauthorized"));
        }
        Integer receiverNo = (Integer) body.get("receiverNo");
        if (receiverNo == null) {
            return ResponseEntity.badRequest().body(Map.of("error","receiverNo is required"));
        }
        String subject = body.get("subject") == null ? null : String.valueOf(body.get("subject"));
        String content = body.get("content") == null ? "" : String.valueOf(body.get("content"));

        NoteMessageEntity saved = svc.send(senderNo, receiverNo, subject, content);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // 보냄(파일 포함 멀티파트)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendWithFiles(
            @RequestParam Integer receiverNo,
            @RequestParam(required=false) String subject,
            @RequestParam(required=false, defaultValue="") String content,
            @RequestPart(name="files", required=false) List<MultipartFile> files
    ) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer senderNo = extractMemberNo(auth);
        if (senderNo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","unauthorized"));
        }
        NoteMessageEntity saved = svc.sendWithFiles(senderNo, receiverNo, subject, content, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /* ============ 받은/보낸함 ============ */

    // 받은쪽지함(로그인 사용자 기준)
    @GetMapping(value="/inbox", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> inbox(@RequestParam(defaultValue="0") int page,
                                   @RequestParam(defaultValue="20") int size){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer me = extractMemberNo(auth);
        if (me == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","unauthorized"));
        }
        Page<NoteMessageEntity> result =
                noteRepo.findByReceiverNoAndReceiverDeletedFalseOrderBySentAtTxtDesc(me, PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }

    // 보낸쪽지함(로그인 사용자 기준)
    @GetMapping(value="/outbox", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> outbox(@RequestParam(defaultValue="0") int page,
                                    @RequestParam(defaultValue="20") int size){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer me = extractMemberNo(auth);
        if (me == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","unauthorized"));
        }
        Page<NoteMessageEntity> result =
                noteRepo.findBySenderNoAndSenderDeletedFalseOrderBySentAtTxtDesc(me, PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }

    /* ============ 읽음/답장/삭제 ============ */

    // 읽음 처리
    @PostMapping(value="/{noteId}/read", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> read(@PathVariable Integer noteId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer me = extractMemberNo(auth);
        if (me == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","unauthorized"));
        }
        svc.markRead(noteId, me);
        return ResponseEntity.ok(Map.of("read", true));
    }

    // 답장 (텍스트 JSON) — 파일 답장은 필요시 멀티파트로 별도 추가 가능
    @PostMapping(value="/{noteId}/reply", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> reply(@PathVariable Integer noteId,
                                   @RequestBody Map<String,String> body){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer senderNo = extractMemberNo(auth);
        if (senderNo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","unauthorized"));
        }
        String content = body.getOrDefault("content","");
        NoteMessageEntity saved = svc.reply(noteId, senderNo, content);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // 개인함 삭제(수신자/발신자 각각의 가림 처리)
    @DeleteMapping(value="/{noteId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteForUser(@PathVariable Integer noteId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer actorNo = extractMemberNo(auth);
        if (actorNo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","unauthorized"));
        }
        svc.deleteForUser(noteId, actorNo);
        return ResponseEntity.ok(Map.of("deleted", true));
    }
}