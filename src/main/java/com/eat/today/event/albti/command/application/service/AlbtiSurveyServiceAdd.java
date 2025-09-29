package com.eat.today.event.albti.command.application.service;

import com.eat.today.event.albti.command.application.dto.AlbtiSurveyAddRequestDTO;
import com.eat.today.event.albti.command.application.dto.AlbtiSurveyAddResponseDTO;
import com.eat.today.event.albti.command.application.entity.Albti;
import com.eat.today.event.albti.command.application.entity.AlbtiSurvey;
import com.eat.today.event.albti.command.domain.repository.AlbtiRepository;
import com.eat.today.event.albti.command.domain.repository.AlbtiSurveyAddRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlbtiSurveyServiceAdd {

    private final AlbtiSurveyAddRepository  albtiSurveyAddRepository;
    private final AlbtiRepository  albtiRepository;


    public AlbtiSurveyAddResponseDTO addSurvey(AlbtiSurveyAddRequestDTO requestDTO) {
        // albti테이블에서 albti_no가 존재하는지 검증 단계
        // 1. albtiNo 유효성 검사
        Albti albti = albtiRepository.findById(requestDTO.getAlbtiNo())
                .orElseThrow(() -> new IllegalArgumentException("해당 BTI 번호가 존재하지 않습니다."));


        // albti번호와 내용을 저장
        // 2. 설문 엔티티 생성 및 값 설정
        AlbtiSurvey survey = new AlbtiSurvey();
        survey.setAlbti(albti);
        survey.setAlbtiSurveyContent(requestDTO.getAlbtiSurveyContent());
        // 3. DB 저장
        AlbtiSurvey saved = albtiSurveyAddRepository.save(survey);

        // 4. 응답 DTO로 변환
        return new AlbtiSurveyAddResponseDTO(
                saved.getAlbtiSurveyNo(),       // 생성된 설문 번호
                saved.getAlbti().getAlbtiNo(),  // 술BTI번호
                saved.getAlbtiSurveyContent()   // 술BTI번호에 따른 설문 문항 내용
        );
    }

}
