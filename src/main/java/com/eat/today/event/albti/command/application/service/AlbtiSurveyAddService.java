package com.eat.today.event.albti.command.application.service;


import com.eat.today.event.albti.command.application.dto.AlbtiSurveyAddRequestDTO;
import com.eat.today.event.albti.command.application.dto.AlbtiSurveyAddResponseDTO;
import com.eat.today.event.albti.command.application.entity.AlbtiSurvey;
import com.eat.today.event.albti.command.domain.repository.AlbtiSurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlbtiSurveyAddService {
    private final AlbtiSurveyRepository albtiSurveyRepository;

    @Transactional
    public AlbtiSurveyAddResponseDTO addSurvey(AlbtiSurveyAddRequestDTO request) {
        // 요청 DTO를 기반으로 엔티티 생성
        AlbtiSurvey survey = AlbtiSurvey.builder()
                .question(request.getQuestion())
                .typeA(request.getTypeA())
                .typeB(request.getTypeB())
                .build();

        // DB에 저장
        AlbtiSurvey saved = albtiSurveyRepository.save(survey);

        // 저장된 엔티티 기반으로 ResponseDTO 반환
        return new AlbtiSurveyAddResponseDTO(
                saved.getAlbtiSurveyNo(),
                saved.getQuestion(),
                saved.getTypeA(),
                saved.getTypeB(),
                "설문이 성공적으로 추가되었습니다."
        );
    }
}
