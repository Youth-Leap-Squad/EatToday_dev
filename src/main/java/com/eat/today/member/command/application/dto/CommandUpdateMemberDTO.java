package com.eat.today.member.command.application.dto;

import lombok.Data;

@Data
public class CommandUpdateMemberDTO {

    private String memberEmail;
    private String memberPw;
    private String memberName;
    private String memberBirth;
    private String memberPhone;
    private String memberStatus;
    private Boolean memberActive;


}
