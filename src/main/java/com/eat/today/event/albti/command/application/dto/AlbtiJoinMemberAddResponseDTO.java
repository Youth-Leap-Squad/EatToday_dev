package com.eat.today.event.albti.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlbtiJoinMemberAddResponseDTO {
    private int albtiMemberNo;  // 참여자 PK
    private int memberNo;
    private int albtiSurveyNo;        // 선택한 설문번호
}
