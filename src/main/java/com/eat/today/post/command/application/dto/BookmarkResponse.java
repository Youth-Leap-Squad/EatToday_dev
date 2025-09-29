package com.eat.today.post.command.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookmarkResponse {
    private Integer boardNo;
    private String  memberId;
    private String  boardTitle;
    private String  foodPicture;
}
