package com.eat.today.member.command.application.dto;

import lombok.Data;
    
@Data
public class WithdrawRequest {
    private String memberEmail;
    private String memberPw;
}