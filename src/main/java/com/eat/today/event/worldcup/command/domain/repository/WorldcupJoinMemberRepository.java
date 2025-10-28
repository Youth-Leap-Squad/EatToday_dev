package com.eat.today.event.worldcup.command.domain.repository;

import com.eat.today.event.worldcup.command.application.entity.WorldcupJoinMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WorldcupJoinMemberRepository extends JpaRepository<WorldcupJoinMember, Integer> {
    // 특정 회원이 특정 월드컵에 참여했는지 조회
    Optional<WorldcupJoinMember> findByMemberNoAndWorldcupNo(int memberNo, int worldcupNo);

    @Query("""
    SELECT COUNT(wjm) > 0
    FROM WorldcupJoinMember wjm
    JOIN Worldcup wc ON wjm.worldcupNo = wc.worldcupNo
    WHERE wjm.memberNo = :memberNo
      AND wjm.alcoholId = :alcoholId
      AND CURRENT_DATE BETWEEN 
          FUNCTION('STR_TO_DATE', wc.worldcupStartDate, '%Y-%m-%d')
      AND FUNCTION('STR_TO_DATE', wc.worldcupFinishDate, '%Y-%m-%d')
""")
    boolean existsThisWeek(int memberNo, int alcoholId);
}
