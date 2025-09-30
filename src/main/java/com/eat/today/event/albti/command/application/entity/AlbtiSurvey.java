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
public class AlbtiSurvey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "albti_survey_no", nullable = false)
    private int albtiSurveyNo;

//    @Column(name = "alBTI_no", nullable = false)
//    private int albtiNo;                    // 술bti 번호(FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alBTI_no", nullable = false)
    private Albti albti;


    @Column(name = "albti_survey_content", nullable = false)
    private String albtiSurveyContent;      // 설문내용




}
