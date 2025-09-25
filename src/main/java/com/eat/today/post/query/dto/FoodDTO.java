// FoodListWithReactionsDTO.java
package com.eat.today.post.query.dto;
import lombok.Data;

@Data
public class FoodDTO {
    private int boardNo;
    private String boardTitle;
    private String foodPicture;
    private String memberId;
    private String boardDate;
    private int boardSeq;
    private int likesNo1;
    private int likesNo2;
    private int likesNo3;
    private int likesNo4;
}
