package com.eat.today.event.albti.command.domain.repository;

import com.eat.today.event.albti.command.application.entity.AlbtiMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbtiMemberRepository extends JpaRepository<AlbtiMember,Integer> {
}
