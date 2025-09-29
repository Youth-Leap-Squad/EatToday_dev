package com.eat.today.event.albti.command.domain.repository;

import com.eat.today.event.albti.command.application.entity.AlbtiSurvey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbtiSurveyAddRepository extends JpaRepository<AlbtiSurvey, Integer> {
}
