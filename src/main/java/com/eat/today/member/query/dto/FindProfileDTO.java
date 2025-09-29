package com.eat.today.member.query.dto;

import lombok.Data;

@Data
public class FindProfileDTO {
    private String memberName;
    private String memberBirth;
    private String memberPhone;
    private int memberLevel;
    private boolean memberActive;
}
