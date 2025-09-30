package com.eat.today.member.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity {

    // 권한 Enum
    public enum Role { ADMIN, USER }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_no")
    private Integer memberNo;

    // 권한
    @Enumerated(EnumType.STRING)
    @Column(name = "member_role", nullable = false)
    private Role memberRole = Role.USER;

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

    @Column(name="member_active", nullable = false)
    private boolean memberActive =true;

    //  신고 상태 + 카운트 필드 추가
    @Column(name = "member_status", nullable = false)
    private String memberStatus = "normal";

    @Column(name = "report_count", nullable = false)
    private Integer reportCount = 0;

    /** 편의 메서드들 */
    public void increaseReportCount() {
        this.reportCount++;
    }

    public void changeStatusByCount() {
        if (reportCount >= 5) this.memberStatus = "영구정지";
        else if (reportCount >= 3) this.memberStatus = "7일 정지";
        else if (reportCount >= 1) this.memberStatus = "1일 정지";
        else this.memberStatus = "normal";
    }
}