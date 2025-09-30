package com.eat.today.event.albti.command.application.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "albti_output")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AlbtiOutput {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "albti_output_no", nullable = false)
    private int albtiOutputNo;

    @Column(name = "alBTI_no", nullable = false)
    private int albtiNo;

    @Column(name = "alBTI_alcohol_explain", nullable = false)
    private String albtiAlcoholExplain;

    @Column(name = "board_no", nullable = false)
    private int boardNo;

    @Column(name = "alBTI_member_no",nullable = false)
    private int albtiMemberNo;


}
