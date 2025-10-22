package com.eat.today.event.albti.command.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlbtiSurveyAddRequestDTO {
    private String question; // 설문 내용
    private int typeA;       // DB에서 int로 저장되는 A 유형 점수
    private int typeB;       // DB에서 int로 저장되는 B 유형 점수
}
