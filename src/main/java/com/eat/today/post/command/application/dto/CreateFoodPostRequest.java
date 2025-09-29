package com.eat.today.post.command.application.dto;

import lombok.Data;

@Data
public class CreateFoodPostRequest {
    private Integer alcoholNo;
    private Integer memberNo;
    private String  boardTitle;
    private String  boardContent;
    private String  foodExplain;
    private String  foodPicture;
    private Integer boardSeq;
}