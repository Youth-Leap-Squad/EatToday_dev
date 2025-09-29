package com.eat.today.event.worldcup.command.application.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "worldcup")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Worldcup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "worldcup_no", nullable = false)
    private int worldcupNo;

    @Column(name = "worldcup_start_date", nullable = false)
    private String worldcupStartDate;

    @Column(name = "worldcup_finish_date")
    private String worldcupFinishDate;

}
