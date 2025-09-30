package com.eat.today.event.albti.command.domain.repository;
import com.eat.today.event.albti.command.application.entity.AlbtiJoinMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbtiJoinMemberRepository extends JpaRepository<AlbtiJoinMember,Integer> {
    // 필요시 회원별 참여 조회 추가 가능
}
