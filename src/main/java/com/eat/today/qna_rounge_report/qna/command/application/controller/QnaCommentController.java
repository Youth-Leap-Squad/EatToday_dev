package com.eat.today.qna_rounge_report.qna.command.application.controller;

import com.eat.today.qna_rounge_report.qna.command.application.dto.*;
import com.eat.today.qna_rounge_report.qna.command.application.service.QnaCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/qna-comments")
public class QnaCommentController {

    private final QnaCommentService service;

    /** 댓글 작성 */
    @PostMapping
    public ResponseEntity<QnaCommentResponse> create(@RequestBody @Valid CreateQnaCommentRequest req) {
        QnaCommentResponse created = service.create(req);
        return ResponseEntity.created(URI.create("/qna-comments/" + created.id()))
                .body(created);
    }

    /** 댓글 수정 */
    @PatchMapping("/{id}")
    public QnaCommentResponse update(@PathVariable Integer id,
                                     @RequestBody @Valid UpdateQnaCommentRequest req) {
        return service.update(id, req);
    }

    /** 댓글 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
