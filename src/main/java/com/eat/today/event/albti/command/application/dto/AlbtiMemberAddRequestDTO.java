package com.eat.today.event.albti.command.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlbtiMemberAddRequestDTO {
    private int memberNo;
    private int albtiSurveyNo;
    private String choice; // 'A' or 'B'

}
