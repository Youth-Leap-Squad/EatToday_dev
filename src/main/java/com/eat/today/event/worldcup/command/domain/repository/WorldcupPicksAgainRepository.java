package com.eat.today.event.worldcup.command.domain.repository;

import com.eat.today.event.worldcup.command.application.entity.WorldcupPicks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorldcupPicksAgainRepository extends JpaRepository<WorldcupPicks, Integer> {

    // 특정 회원이 선택한 데이터 조회
//    List<WorldcupPicks> findByworldcupJoinMemberNo(int worldcupJoinMemberNo);

    // 특정 회원 선택 전체 삭제
    void deleteByworldcupJoinMemberNo(int worldcupJoinMemberNo);

}
