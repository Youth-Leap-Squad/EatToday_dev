package com.eat.today.event.albti.command.application.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "albti_answer")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class AlbtiAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "albti_answer_no")
    private int answerNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no", nullable = false)
    private AlbtiMember memberNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "albti_survey_no", nullable = false)
    private AlbtiSurvey albtiSurvey;

    @Column(nullable = false)
    private String choice; // 'A' 또는 'B'
}
