package com.eat.today.event.albti.command.application.service;

import com.eat.today.event.albti.command.application.dto.AlbtiMemberAddRequestDTO;
import com.eat.today.event.albti.command.application.dto.AlbtiMemberAddResponseDTO;
import com.eat.today.event.albti.command.application.entity.AlbtiAnswer;
import com.eat.today.event.albti.command.domain.repository.AlbtiAnswerRepository;
import com.eat.today.event.albti.command.domain.repository.AlbtiMemberRepository;
import com.eat.today.event.albti.command.domain.repository.AlbtiSurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlbtiMemberAddService {

    private final AlbtiAnswerRepository albtiAnswerRepository;
    private final AlbtiMemberRepository albtiMemberRepository;
    private final AlbtiSurveyRepository albtiSurveyRepository;

    @Transactional
    public AlbtiMemberAddResponseDTO addAlbtiMember(AlbtiMemberAddRequestDTO request) {

        // 1. 신규 AlbtiAnswer 엔티티 생성
        AlbtiAnswer answer = new AlbtiAnswer();

        // 2. 회원 정보 확인 및 매핑
        answer.setMemberNo(
                albtiMemberRepository.findById(request.getMemberNo())
                        .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."))
        );

        // 3. 설문 정보 확인 및 매핑
        answer.setAlbtiSurvey(
                albtiSurveyRepository.findById(request.getAlbtiSurveyNo())
                        .orElseThrow(() -> new RuntimeException("설문 정보를 찾을 수 없습니다."))
        );

        // 4. 사용자의 선택값 저장 ('A' 또는 'B')
        answer.setChoice(request.getChoice());

        // 5. 저장 (1개의 설문만 저장)
        AlbtiAnswer savedAnswer = albtiAnswerRepository.save(answer);

        // 6. 응답 DTO 반환
        return new AlbtiMemberAddResponseDTO(
                savedAnswer.getAnswerNo(),
                savedAnswer.getMemberNo().getMemberNo(),
                savedAnswer.getAlbtiSurvey().getAlbtiSurveyNo(),
                savedAnswer.getChoice(),
                "설문 참여 기록이 정상적으로 저장되었습니다."
        );
    }
}