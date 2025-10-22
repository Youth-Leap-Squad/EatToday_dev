package com.eat.today.sns.command.application.controller.message;

import com.eat.today.sns.command.application.entity.message.NoteMessageEntity;
import com.eat.today.sns.command.domain.repository.message.NoteMessageRepo;
import com.eat.today.sns.command.domain.service.NoteCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteCommandController {
    private final NoteCommandService svc;
    private final NoteMessageRepo noteRepo;

    // 보냄(텍스트)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public NoteMessageEntity send(@RequestBody Map<String,Object> body){
        return svc.send((Integer)body.get("senderNo"), (Integer)body.get("receiverNo"),
                (String)body.get("subject"), (String)body.get("content"));
    }

    // 보냄(파일 포함)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public NoteMessageEntity sendWithFiles(@RequestParam Integer senderNo,
                                           @RequestParam Integer receiverNo,
                                           @RequestParam(required=false) String subject,
                                           @RequestParam(required=false, defaultValue="") String content,
                                           @RequestPart(name="files", required=false) List<MultipartFile> files) throws Exception {
        return svc.sendWithFiles(senderNo, receiverNo, subject, content, files);
    }

    // 받은쪽지함
    @GetMapping("/inbox")
    public Page<NoteMessageEntity> inbox(@RequestParam Integer memberNo,
                                         @RequestParam(defaultValue="0") int page,
                                         @RequestParam(defaultValue="20") int size){
        return noteRepo.findByReceiverNoAndReceiverDeletedFalseOrderBySentAtTxtDesc(memberNo, PageRequest.of(page, size));
    }

    // 보낸쪽지함
    @GetMapping("/outbox")
    public Page<NoteMessageEntity> outbox(@RequestParam Integer memberNo,
                                          @RequestParam(defaultValue="0") int page,
                                          @RequestParam(defaultValue="20") int size){
        return noteRepo.findBySenderNoAndSenderDeletedFalseOrderBySentAtTxtDesc(memberNo, PageRequest.of(page, size));
    }

    // 읽음 처리
    @PostMapping("/{noteId}/read")
    public void read(@PathVariable Integer noteId, @RequestParam Integer memberNo){ svc.markRead(noteId, memberNo); }

    // 답장
    @PostMapping("/{noteId}/reply")
    public NoteMessageEntity reply(@PathVariable Integer noteId,
                                   @RequestParam Integer senderNo,
                                   @RequestBody Map<String,String> body){
        return svc.reply(noteId, senderNo, body.getOrDefault("content",""));
    }

    // 개인함 삭제
    @DeleteMapping("/{noteId}")
    public void deleteForUser(@PathVariable Integer noteId, @RequestParam Integer actorNo){
        svc.deleteForUser(noteId, actorNo);
    }
}