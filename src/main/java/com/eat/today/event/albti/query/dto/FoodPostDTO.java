package com.eat.today.event.albti.query.dto;

import lombok.Data;

@Data
public class FoodPostDTO {
    private int board_no;
    private int alcohol_no;
    private String board_title;
    private String board_content;
    private String food_explain;
    private int member_no;
    private String food_picture;
    private String board_date;
    private int board_seq;
    private boolean confirmed_yn;

}
