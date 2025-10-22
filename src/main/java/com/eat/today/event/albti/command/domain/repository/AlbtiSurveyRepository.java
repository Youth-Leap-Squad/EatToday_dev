package com.eat.today.event.albti.command.domain.repository;

import com.eat.today.event.albti.command.application.entity.Albti;
import com.eat.today.event.albti.command.application.entity.AlbtiSurvey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbtiSurveyRepository extends JpaRepository<AlbtiSurvey,Integer> {
}
