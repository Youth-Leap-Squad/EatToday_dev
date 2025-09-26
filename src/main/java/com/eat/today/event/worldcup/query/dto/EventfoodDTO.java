package com.eat.today.event.worldcup.query.dto;

import lombok.Data;

@Data
public class EventfoodDTO {
    private int food_no;
    private int board_no;
    private String food_content;
    private int num_of_wins;
    private String worldcup_winning_food;

}
