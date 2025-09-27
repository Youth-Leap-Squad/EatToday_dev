package com.eat.today.member.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="member")
public class  MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_no")
    private Long memberNo;

    @Column(name="member_phone" ,nullable = false)
    private String memberPhone;
//    @Column(name="member_pw" ,nullable = false, unique = true)
//    private String memberPw;
    @Column(name = "encrypt_pwd", nullable = false)
    private String encryptPwd;

    @Column(name="member_name" ,nullable = false)
    private String memberName;

    @Column(name="member_id" ,nullable = false, unique = true)
    private String memberId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_role_no", nullable = false)
//    private RoleEntity roleNo;
//    @Column(name="member_birth" ,nullable = false)
//    private String memberBirth;
//    @Column(name="member_status" ,nullable = false)
//    private String memberStatus;
//    @Column(name="member_active" ,nullable = false)
//    private boolean memberActive;
//    @Column(name="member_at" ,nullable = false)
//    private String memberAt;
//    @Column(name="member_level" ,nullable = true)
//    private int memberLevel;

}
