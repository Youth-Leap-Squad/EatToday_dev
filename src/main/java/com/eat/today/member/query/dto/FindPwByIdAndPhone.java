package com.eat.today.member.query.dto;

import lombok.Data;

@Data
public class FindPwByIdAndPhone {
    private int memberNo;
    private int memberRoleNo;
    private String memberId;
    private String memberPw;
    private String memberName;
    private String memberBirth;
    private String memberPhone;
    private String memberStatus;
    private boolean memberActive;
    private String memberAt;
    private int memberLevel;
}
