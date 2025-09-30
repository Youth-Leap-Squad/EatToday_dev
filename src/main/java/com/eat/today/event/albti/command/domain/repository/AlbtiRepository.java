package com.eat.today.event.albti.command.domain.repository;

import com.eat.today.event.albti.command.application.entity.Albti;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbtiRepository extends JpaRepository<Albti, Integer> {
}
