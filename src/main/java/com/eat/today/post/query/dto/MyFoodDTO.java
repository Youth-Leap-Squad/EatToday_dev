package com.eat.today.post.query.dto;

import lombok.Data;

@Data
public class MyFoodDTO {
    private Integer boardNo;
    private Integer alcoholNo;
    private String  boardTitle;
    private String  boardContent;
    private String  foodExplain;
    private String  memberId;
    private String  profileImageUrl;
    private String  foodPicture;
    private String  boardDate;
    private Integer boardSeq;
    private Integer likesNo1;
    private Integer likesNo2;
    private Integer likesNo3;
    private Integer likesNo4;
    private Boolean confirmedYn;
}
