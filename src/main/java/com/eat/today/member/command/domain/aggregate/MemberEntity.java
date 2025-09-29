package com.eat.today.member.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="member")
public class  MemberEntity {

    public enum Role { ADMIN, USER }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_no")
    private Long memberNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role", nullable = false)
    private Role memberRole;

    @Column(name="member_phone", nullable = false)
    private String memberPhone;

    @Column(name="member_pw", nullable = false)
    private String memberPw;

    @Column(name="member_name", nullable = false)
    private String memberName;

    @Column(name="member_id", nullable = false, unique = true)
    private String memberId;

    @Column(name="member_birth", nullable = false)
    private String memberBirth;

}

