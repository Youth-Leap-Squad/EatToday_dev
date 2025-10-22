package com.eat.today.event.albti.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlbtiMemberAddResponseDTO {
    private int answerNo;
    private int memberNo;
    private int albtiSurveyNo;
    private String choice; // 'A' or 'B'
    private String message;    // 추가 성공 여부 등 안내 메시지
}
