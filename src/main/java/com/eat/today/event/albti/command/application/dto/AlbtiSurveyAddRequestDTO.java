package com.eat.today.event.albti.command.application.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlbtiSurveyAddRequestDTO {
    private int albtiNo;                // 어떤 술BTI에 속하는 설문인지
    private String albtiSurveyContent;  // 술BTI번호에 따른 설문 문항 내용
}
