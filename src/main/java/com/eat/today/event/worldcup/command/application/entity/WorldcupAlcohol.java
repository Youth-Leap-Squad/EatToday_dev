package com.eat.today.event.worldcup.command.application.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "worldcup_alcohol")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class WorldcupAlcohol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "worldcup_alcohol_no", nullable = false)
    private int worldcupAlcoholNo;

    @Column(name = "alcohol_no", nullable = false)
    private int alcoholNo;

    @Column(name = "worldcup_no", nullable = false)
    private int worldcupNo;
}
