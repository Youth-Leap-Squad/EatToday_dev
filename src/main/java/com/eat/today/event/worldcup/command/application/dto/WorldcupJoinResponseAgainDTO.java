package com.eat.today.event.worldcup.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorldcupJoinResponseAgainDTO {
    private int memberNo;
    private int alcoholId;
    private int foodId;
    private String message;
}
