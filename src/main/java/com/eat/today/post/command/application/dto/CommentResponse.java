package com.eat.today.post.command.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentResponse {
    Integer foodCommentNo;
    Integer boardNo;
    Integer memberNo;
    String  content;
    String  createdAt;
    String  updatedAt;
}
