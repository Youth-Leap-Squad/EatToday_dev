package com.eat.today.post.command.application.dto;

import lombok.Data;

@Data
public class UpdateCommentRequest {
    private Integer memberNo;
    private String content;
}
