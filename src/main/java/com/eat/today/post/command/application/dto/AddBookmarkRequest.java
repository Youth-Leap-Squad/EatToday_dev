package com.eat.today.post.command.application.dto;

import lombok.Data;

@Data
public class AddBookmarkRequest {
    private Integer memberNo;
    private Integer boardNo;
}
