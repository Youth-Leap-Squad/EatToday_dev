package com.eat.today.post.command.application.dto;

import lombok.Data;

@Data
public class UpdateFoodPostRequest {
    private String boardTitle;
    private String boardContent;
    private String foodExplain;
    private String foodPicture;
}
