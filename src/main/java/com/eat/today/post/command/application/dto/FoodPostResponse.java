package com.eat.today.post.command.application.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FoodPostResponse {
    private Integer boardNo;
    private Integer alcoholNo;
    private Integer memberNo;
    private String memberId;
    private String boardTitle;
    private String boardContent;
    private String foodExplain;
    private List<String> foodPictures;
    private String boardDate;
    private Integer boardSeq;
    private Boolean confirmedYn;
    private Integer likeNo1;
    private Integer likeNo2;
    private Integer likeNo3;
    private Integer likeNo4;
}
