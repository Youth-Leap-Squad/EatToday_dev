package com.eat.today.event.albti.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlbtiSurveyAddResponseDTO {
    private int albtiSurveyNo;          // 생성된 설문 번호
    private int albtiNo;                // BTI 번호
    private String albtiSurveyContent;  // 설문 내용
}
