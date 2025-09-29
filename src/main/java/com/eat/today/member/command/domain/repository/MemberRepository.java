package com.eat.today.member.command.domain.repository;

import com.eat.today.member.command.domain.aggregate.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

}
