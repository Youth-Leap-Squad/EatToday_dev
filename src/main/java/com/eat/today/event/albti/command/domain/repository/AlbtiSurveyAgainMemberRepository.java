package com.eat.today.event.albti.command.domain.repository;

import com.eat.today.event.albti.command.application.entity.AlbtiJoinMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//@Repository
public interface AlbtiSurveyAgainMemberRepository extends JpaRepository<AlbtiJoinMember, Integer> {

    // 회원별 참여 기록 삭제
//    @Transactional
//    @Modifying
//    @Query("DELETE FROM AlbtiJoinMember a WHERE a.member.memberNo = :memberNo")
//    void deleteByMemberNo(@Param("memberNo") int memberNo);

//    @Transactional
//    void deleteByMember_MemberNo(int memberNo); // 회원 기존 선택 삭제


    // 회원 번호로 AlbtiJoinMember 찾기
    Optional<AlbtiJoinMember> findByMemberMemberNo(int memberNo);
}
