// CreateCommentOnFoodRequest.java
package com.eat.today.post.command.application.dto;

import lombok.Data;

@Data
public class CreateCommentOnFoodRequest {
    private Integer memberNo;
    private String  content;
}
