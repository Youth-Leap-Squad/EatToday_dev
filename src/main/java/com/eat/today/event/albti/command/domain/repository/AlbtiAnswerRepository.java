package com.eat.today.event.albti.command.domain.repository;

import com.eat.today.event.albti.command.application.entity.AlbtiAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlbtiAnswerRepository extends JpaRepository<AlbtiAnswer, Integer> {

    // 설문 중복 방지 / 특정 문항 재선택 시 사용 가능
    Optional<AlbtiAnswer> findByMemberNo_MemberNoAndAlbtiSurvey_AlbtiSurveyNo(int memberNo, int surveyNo);

    // ✅ 재검사 시 해당 회원 답변 전체 삭제
    void deleteByMemberNo_MemberNo(int memberNo);
}
