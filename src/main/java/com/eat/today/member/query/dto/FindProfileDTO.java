package com.eat.today.member.query.dto;

import lombok.Data;

@Data
public class FindProfileDTO {
    private String memberId;
    private String memberBirth;
    private String memberPhone;
    private String memberEmail;
    private int memberLevel;
    private boolean memberActive;
    private String profileImageUrl; // 프로필 이미지 URL (추가)
}
