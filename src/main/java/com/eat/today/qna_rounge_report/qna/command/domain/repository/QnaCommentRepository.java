package com.eat.today.qna_rounge_report.qna.command.domain.repository;

import com.eat.today.qna_rounge_report.qna.command.domain.aggregate.QnaComment;

import java.util.Optional;

public interface QnaCommentRepository {
    QnaComment save(QnaComment comment);
    Optional<QnaComment> findById(Integer id);
    void deleteById(Integer id);
}
