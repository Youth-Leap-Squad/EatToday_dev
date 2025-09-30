package com.eat.today.event.worldcup.command.domain.repository;

import com.eat.today.event.worldcup.command.application.entity.WorldcupPicks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorldcupPicksRepository extends JpaRepository<WorldcupPicks, Integer> {
}
