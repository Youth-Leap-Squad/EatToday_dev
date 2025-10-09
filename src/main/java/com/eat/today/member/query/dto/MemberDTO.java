package com.eat.today.member.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class MemberDTO {

    private int memberNo;
    private String memberRole;
    private int memberRoleNo;
    private String memberId;
    private String memberPw;
    private String memberName;
    private String memberBirth;
    private String memberPhone;
    private String memberEmail;
    private String memberStatus;
    private boolean memberActive;
    private String memberAt;
    private int memberLevel;

    private MemberDTO role;   // FK 매핑
}
