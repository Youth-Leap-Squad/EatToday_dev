package com.eat.today.post.command.application.dto;

import lombok.Data;

@Data
public class AddCommentRequest {
    private Integer boardNo;
    private Integer memberNo;
    private String  content;
}