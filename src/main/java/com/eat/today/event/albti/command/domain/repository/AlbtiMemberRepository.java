package com.eat.today.event.albti.command.domain.repository;

import com.eat.today.event.albti.command.application.entity.AlbtiMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbtiMemberRepository extends JpaRepository<AlbtiMember, Integer> {
    // 기본 CRUD는 JpaRepository에서 제공
    // 필요시 추가 커스텀 쿼리 메서드 작성 가능
}