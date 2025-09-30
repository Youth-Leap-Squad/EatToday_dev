package com.eat.today.member.command.domain.repository;

import com.eat.today.member.command.domain.aggregate.SecessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecessionRepository extends JpaRepository<SecessionEntity, Long> {

}