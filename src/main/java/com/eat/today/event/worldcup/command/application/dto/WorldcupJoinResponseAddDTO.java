package com.eat.today.event.worldcup.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorldcupJoinResponseAddDTO {
    private int memberNo;
    private int worldcupNo;
    private int alcoholId;
    private int foodId;
    private int worldcupJoinMemberNo;   // 새로 생성된 joinMember PK
    private String message;
}
