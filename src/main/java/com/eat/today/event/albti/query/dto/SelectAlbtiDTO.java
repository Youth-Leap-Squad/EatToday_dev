package com.eat.today.event.albti.query.dto;

import lombok.Data;

@Data
public class SelectAlbtiDTO {
    private AlbtiJoinMemberDTO albti_join_member;
    private AlbtiDTO albti_dto;
    private AlbtiOutputDTO albti_output;
    private FoodPostDTO foodpost_dto;

}
