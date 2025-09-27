package com.eat.today.qna_rounge_report.qna.command.domain.repository;

import com.eat.today.qna_rounge_report.qna.command.domain.aggregate.QnaPost;

import java.util.Optional;

public interface QnaPostRepository {
    QnaPost save(QnaPost qnaPost);
    Optional<QnaPost> findById(Integer id);
    void deleteById(Integer id);
}
