package com.eat.today.event.albti.command.application.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "albti_join_member")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AlbtiJoinMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alBTI_member_no", nullable = false)
    private int albtiMemberNo;

//    @Column(name = "member_no", nullable = false)
//    private int memberNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no", nullable = false)
    private AlbtiMember member;  // 참여한 회원


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "albti_survey_no", nullable = false)
    private AlbtiSurvey albtiSurvey;

}
