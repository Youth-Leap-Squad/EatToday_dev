package com.eat.today.event.albti.command.domain.repository;

import com.eat.today.event.albti.command.application.entity.AlbtiJoinMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;


public interface AlbtiJoinMemberRepository extends JpaRepository<AlbtiJoinMember, Integer> {

    boolean existsByMemberNo(int memberNo); // ✅ 중복검사 가능

    Optional<AlbtiJoinMember> findByMemberNo(int memberNo);  // ✅ 추가

    // 회원이 오늘 참여했는지 확인하는 메소드
    boolean existsByMemberNoAndParticipatedAt(int memberNo, LocalDate participatedAt);


    // 회원번호로 AlbtiJoinMember 찾기
//    Optional<AlbtiJoinMember> findByMemberMemberNo(int memberNo);

}
