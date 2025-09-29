package com.eat.today.event.worldcup.command.domain.repository;

import com.eat.today.event.worldcup.command.application.entity.WorldcupJoinMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorldcupJoinMemberRepository extends JpaRepository<WorldcupJoinMember, Integer> {
    // 특정 회원이 특정 월드컵에 참여했는지 조회
    Optional<WorldcupJoinMember> findByMemberNoAndWorldcupNo(int memberNo, int worldcupNo);
}
