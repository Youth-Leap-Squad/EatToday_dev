package com.eat.today.member.command.application.dto;

import lombok.Data;

@Data
public class CommandRequestRegisterMemberDTO {
    private String memberPhone;
    private String memberPw;
    private String memberName;


    private String memberId;
    private String memberBirth;
}
