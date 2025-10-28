package com.eat.today.event.worldcup.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorldcupJoinRequestAddAgainDTO {
    private int memberNo;
    private int worldcupNo;
    private int alcoholId;
    private int foodId;
}
