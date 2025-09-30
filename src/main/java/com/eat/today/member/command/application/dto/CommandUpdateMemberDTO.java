package com.eat.today.member.command.application.dto;

import lombok.Data;

@Data
public class CommandUpdateMemberDTO {

    private String memberPhone;
    private String memberName;
    private String memberBirth;
    private Boolean memberActive;

}
