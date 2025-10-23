package com.eat.today.event.albti.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbtiAnswerAgainResponceDTO {
    private int answerNo;
    private int memberNo;   // 참여할 회원번호
    private int albtiSurveyNo;  // 새로 선택할 값에 대한 설문번호
    private String choice; // 'A' or 'B'이고, 새로 선택한 값(바뀐 값)
    private String message;    // 추가 성공 여부 등 안내 메시지

}
