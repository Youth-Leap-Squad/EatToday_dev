package com.eat.today.event.albti.query.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectAlbtiDTO {


    private AlbtiJoinMemberDTO albti_join_member;
    private AlbtiDTO albti_dto;
    private AlbtiOutputDTO albti_output;
    private FoodPostDTO foodpost_dto;
//    private AlbtiJoinMemberDTO albtiJoinMember;
//    private AlbtiDTO albtiDto;             // 단일 객체
//    private AlbtiOutputDTO albtiOutput;    // 단일 객체
//    private FoodPostDTO foodPostDto;       // 단일 객체

}
