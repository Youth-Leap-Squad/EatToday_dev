// FoodCommentDTO.java
package com.eat.today.post.query.dto;
import lombok.Data;

@Data
public class FoodCommentDTO {
    private int foodCommentNo;
    private String memberId;
    private String fcContent;
    private String fcDate;
}
