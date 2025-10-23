package com.eat.today.event.albti.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlbtiSurveyAddResponseDTO {
    private int albtiSurveyNo; // DB에서 생성된 설문 번호
    private String question;   // 설문 내용
    private int typeA;         // typeA 값
    private int typeB;         // typeB 값
    private String message;    // 추가 성공 여부 등 안내 메시지

}
