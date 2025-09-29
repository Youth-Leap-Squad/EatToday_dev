package com.eat.today.event.albti.command.application.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AlbtiMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_no")
    private int memberNo;  // 회원번호 (PK)

    @Column(name = "member_role", nullable = false)
    private String memberRole;  // 권한번호 (FK: role)

    @Column(name = "member_id", nullable = false, length = 255)
    private String memberId;   // 아이디

    @Column(name = "member_pw", nullable = false, length = 255)
    private String memberPw;   // 비밀번호

    @Column(name = "member_name", nullable = false, length = 255)
    private String memberName; // 회원명

    @Column(name = "member_birth", nullable = false, length = 255)
    private String memberBirth; // 생년월일

    @Column(name = "member_phone", nullable = false, length = 255)
    private String memberPhone; // 핸드폰 번호

    @Column(name = "member_status", nullable = false, length = 255)
    private String memberStatus = "normal"; // 회원 상태

    @Column(name = "member_active", nullable = false)
    private boolean memberActive = true; // 회원 활동 여부

    @Column(name = "member_at", nullable = false, length = 255)
    private String memberAt = "0"; // 계정 생성일

    @Column(name = "member_level")
    private Integer memberLevel; // 회원 등급
}