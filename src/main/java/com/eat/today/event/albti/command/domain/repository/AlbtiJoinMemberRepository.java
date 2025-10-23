package com.eat.today.event.albti.command.domain.repository;

import com.eat.today.event.albti.command.application.entity.AlbtiJoinMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AlbtiJoinMemberRepository extends JpaRepository<AlbtiJoinMember, Integer> {

    // 회원번호로 AlbtiJoinMember 찾기
//    Optional<AlbtiJoinMember> findByMemberMemberNo(int memberNo);

}
