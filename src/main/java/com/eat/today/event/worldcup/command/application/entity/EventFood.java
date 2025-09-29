package com.eat.today.event.worldcup.command.application.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "eventfood")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EventFood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_no" ,nullable = false)
    private int foodNo;

    @Column(name = "board_no", nullable = false)
    private int boardNo;

    @Column(name = "food_content", nullable = false)
    private String foodContent;

    @Column(name = "num_of_wins", nullable = false)
    private int numOfWins;

    @Column(name = "worldcup_winning_food")
    private String worldcupWinningFood;

}
