package com.eat.today.event.worldcup.command.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorldcupJoinRequestAddAgainDTO {
    private int memberNo;
    private int worldcupNo;
    private int alcoholId;
    private int foodId;
}
