package com.eat.today.member.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Member;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
public class MemberDTO {

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

    private RoleDTO role;   // FK 매핑
}
