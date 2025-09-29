package com.eat.today.member.command.domain.repository;

import com.eat.today.member.command.domain.aggregate.MemberEntity;

import java.util.Optional;

public interface ReportMemberRepository {
    Optional<MemberEntity> findById(Integer id);
    MemberEntity save(MemberEntity m);

    int bumpCountAndStatus(Integer id);

}
