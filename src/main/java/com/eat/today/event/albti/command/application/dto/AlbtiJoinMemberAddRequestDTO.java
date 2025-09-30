package com.eat.today.event.albti.command.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlbtiJoinMemberAddRequestDTO {
    private int memberNo;  // 참여할 회원 번호
    private int albtiSurveyNo;   // 선택한 설문번호

}
