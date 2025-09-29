package com.eat.today.qna_rounge_report.qna.command.infrastructure.persistence;

import com.eat.today.qna_rounge_report.qna.command.domain.aggregate.QnaComment;
import com.eat.today.qna_rounge_report.qna.command.domain.repository.QnaCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QnaCommentRepositoryJpaAdapter implements QnaCommentRepository {
    private final SpringDataQnaCommentRepository delegate;

    @Override
    public QnaComment save(QnaComment comment) {
        return delegate.save(comment);
    }

    @Override
    public Optional<QnaComment> findById(Integer id) {
        return delegate.findById(id);
    }

    @Override
    public void deleteById(Integer id) {
        delegate.deleteById(id);
    }
}
