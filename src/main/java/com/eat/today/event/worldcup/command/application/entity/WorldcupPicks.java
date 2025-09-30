package com.eat.today.event.worldcup.command.application.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "world_cup_picks")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class WorldcupPicks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "world_cup_picks_no", nullable = false)
    private int worldcupPicksNo;

    @Column(name = "worldcup_join_member_no", nullable = false)
    private int worldcupJoinMemberNo;

    @Column(name = "worldcup_alcohol_no", nullable = false)
    private int worldcupAlcoholNo;

    @Column(name = "individual_food", nullable = false)
    private int individualFood;
}
