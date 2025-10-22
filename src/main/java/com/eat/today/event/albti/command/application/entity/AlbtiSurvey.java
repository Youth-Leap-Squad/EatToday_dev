package com.eat.today.event.albti.command.application.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "albti_survey")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class AlbtiSurvey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "albti_survey_no")
    private int albtiSurveyNo;

    @Column(nullable = false)
    private String question;

    @Column(name = "type_a", nullable = false)
    private int typeA;

    @Column(name = "type_b", nullable = false)
    private int typeB;
}
