package com.eat.today.event.albti.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbtiSurveyAgainRequestDTO {
    private int memberNo;       // 참여할 회원 번호
    private int newSurveyNo;  // 새로 선택할 설문번호
}
