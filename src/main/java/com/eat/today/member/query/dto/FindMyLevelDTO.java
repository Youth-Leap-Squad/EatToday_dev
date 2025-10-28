package com.eat.today.member.query.dto;

import lombok.Data;

@Data
public class FindMyLevelDTO {
    private String memberId;
    private String memberLevel;
    private String memberRole;
    private String memberLevelLabel;
}
