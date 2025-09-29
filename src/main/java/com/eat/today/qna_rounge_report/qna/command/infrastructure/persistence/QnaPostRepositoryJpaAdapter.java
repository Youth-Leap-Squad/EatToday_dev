package com.eat.today.qna_rounge_report.qna.command.infrastructure.persistence;

import com.eat.today.qna_rounge_report.qna.command.domain.aggregate.QnaPost;
import com.eat.today.qna_rounge_report.qna.command.domain.repository.QnaPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QnaPostRepositoryJpaAdapter implements QnaPostRepository {

    private final SpringDataQnaPostRepository delegate;

    @Override
    public QnaPost save(QnaPost qnaPost) {
        return delegate.save(qnaPost);
    }

    @Override
    public Optional<QnaPost> findById(Integer id) {
        return delegate.findById(id);
    }

    @Override
    public void deleteById(Integer id) {
        delegate.deleteById(id);
    }
}
