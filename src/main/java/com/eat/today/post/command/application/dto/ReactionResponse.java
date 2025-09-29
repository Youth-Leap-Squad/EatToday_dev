package com.eat.today.post.command.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReactionResponse {
    private Integer boardNo;
    private String  boardTitle;
    private String  boardContent;
    private String  foodExplain;
    private String  foodPicture;
    private String  memberId;
    private String  boardDate;
    private Integer boardSeq;
    private Integer likesNo1;
    private Integer likesNo2;
    private Integer likesNo3;
    private Integer likesNo4;
}
