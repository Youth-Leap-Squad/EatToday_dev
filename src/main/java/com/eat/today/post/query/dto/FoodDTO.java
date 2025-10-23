package com.eat.today.post.query.dto;

import lombok.Data;

@Data
public class FoodDTO {

    private Integer boardNo;
    private Integer alcoholNo;
    private Integer memberNo;
    private String boardTitle;
    private String boardContent;
    private String foodExplain;
    private String foodPicture;
    private String confirmedYn;
    private String boardDate;

    private Integer boardSeq;

    private Integer likesNo1;
    private Integer likesNo2;
    private Integer likesNo3;
    private Integer likesNo4;

    private Integer totalLikes;
    private Integer commentCount;
}
