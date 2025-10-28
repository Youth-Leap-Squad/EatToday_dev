package com.eat.today.event.albti.command.application.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "albti_join_member")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class AlbtiJoinMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alBTI_member_no", nullable = false)
    private int albtiMemberNo;


//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_no", nullable = false)
//    private AlbtiMember memberNo;  // 참여한 회원

    @JoinColumn(name = "member_no", nullable = false)
    private int memberNo;  // ✅ 외래키로 숫자만 저장, 그냥 숫자로 보관 (Member 엔티티 연관 X)

    @Column(name = "participated_at")
    private LocalDate participatedAt;

}
