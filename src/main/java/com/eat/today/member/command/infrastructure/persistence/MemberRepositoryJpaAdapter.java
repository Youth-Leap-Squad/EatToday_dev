package com.eat.today.member.command.infrastructure.persistence;

import com.eat.today.member.command.domain.aggregate.MemberEntity;
import com.eat.today.member.command.domain.repository.ReportMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryJpaAdapter implements ReportMemberRepository {
    private final SpringDataMemberRepository delegate;

    @Override public Optional<MemberEntity> findById(Integer id) {
        return delegate.findById(id);
    }
    @Override public MemberEntity save(MemberEntity m) {
        return delegate.save(m);
    }

    @Override
    public int bumpCountAndStatus(Integer id) {
        return delegate.bumpCountAndStatus(id);
    }
}