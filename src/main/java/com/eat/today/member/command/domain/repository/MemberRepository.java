package com.eat.today.member.command.domain.repository;

import com.eat.today.member.command.domain.aggregate.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    List<MemberEntity> findByMemberPhone(String memberPhone);
}
