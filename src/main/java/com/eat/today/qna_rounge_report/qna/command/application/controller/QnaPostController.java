package com.eat.today.qna_rounge_report.qna.command.application.controller;

import com.eat.today.qna_rounge_report.qna.command.application.dto.CreateQnaPostRequest;
import com.eat.today.qna_rounge_report.qna.command.application.dto.QnaPostResponse;
import com.eat.today.qna_rounge_report.qna.command.application.service.QnaPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/qna-posts")
@RequiredArgsConstructor
public class QnaPostController {

    private final QnaPostService qnaPostService;

    /** 문의사항 작성 */
    @PostMapping
    public ResponseEntity<QnaPostResponse> create(@RequestBody @Valid CreateQnaPostRequest req) {
        QnaPostResponse created = qnaPostService.create(req);
        return ResponseEntity.created(URI.create("/api/qna-posts/" + created.id()))
                .body(created);
    }

    /** 문의사항 수정 */
    @PatchMapping("/{id}")
    public QnaPostResponse update(@PathVariable Integer id,
                                  @RequestParam String newContent) {
        return qnaPostService.update(id, newContent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        qnaPostService.delete(id);
        return ResponseEntity.noContent().build();
    }
}