package com.eat.today.post.query.dto;

import lombok.Data;

@Data
public class PopularFoodDTO {
    private int boardNo;
    private String boardTitle;
    private String foodPicture;
    private String memberId;
    private String  profileImageUrl;
    private int totalLikes;
}
