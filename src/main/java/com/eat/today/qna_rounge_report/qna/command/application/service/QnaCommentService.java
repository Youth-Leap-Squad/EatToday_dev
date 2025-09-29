package com.eat.today.qna_rounge_report.qna.command.application.service;

import com.eat.today.qna_rounge_report.qna.command.application.dto.CreateQnaCommentRequest;
import com.eat.today.qna_rounge_report.qna.command.application.dto.QnaCommentResponse;
import com.eat.today.qna_rounge_report.qna.command.application.dto.UpdateQnaCommentRequest;
import com.eat.today.qna_rounge_report.qna.command.domain.aggregate.QnaComment;
import com.eat.today.qna_rounge_report.qna.command.domain.repository.QnaCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class QnaCommentService {

    private final QnaCommentRepository repo;

    public QnaCommentResponse create(CreateQnaCommentRequest req) {
        QnaComment saved = repo.save(
                new QnaComment(req.qnaPostNo(), req.commentMemberNo(),
                        req.comment(), req.createdAt())
        );
        return QnaCommentResponse.from(saved);
    }

    public QnaCommentResponse update(Integer id, UpdateQnaCommentRequest req) {
        QnaComment found = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글 없음: " + id));
        found.updateComment(req.comment());
        return QnaCommentResponse.from(found);
    }

    public void delete(Integer id) {
        repo.findById(id).orElseThrow(() -> new IllegalArgumentException("댓글 없음: " + id));
        repo.deleteById(id);
    }
}