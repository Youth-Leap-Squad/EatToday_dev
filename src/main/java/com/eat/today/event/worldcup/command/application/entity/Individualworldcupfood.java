package com.eat.today.event.worldcup.command.application.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Individual_world_cup_food")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Individualworldcupfood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Individual_food", nullable = false)
    private int individualFood;

    @Column(name = "worldcup_no", nullable = false)
    private int worldcupNo;

    @Column(name = "food_no", nullable = false)
    private int foodNo;

}
