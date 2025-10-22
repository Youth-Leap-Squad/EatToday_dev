package com.eat.today.event.albti.command.domain.repository;

import com.eat.today.event.albti.command.application.entity.AlbtiAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlbtiAnswerRepository extends JpaRepository<AlbtiAnswer, Integer> {
    Optional<AlbtiAnswer> findByMemberNo_MemberNoAndAlbtiSurvey_AlbtiSurveyNo(int memberNo, int surveyNo);

}
