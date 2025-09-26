// FoodPostLikesStatDTO.java
package com.eat.today.post.query.dto;
import lombok.Data;

@Data
public class FoodPostLikesStatDTO {
    private String likesType;     // 컬럼 타입에 맞게 String/Integer 중 선택
    private int reactionCount;
}
