package com.eat.today.member.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "secession")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SecessionEntity {

    @Id
    @Column(name = "member_no")
    private Integer memberNo;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "member_no", nullable = false)
    private MemberEntity member;

    @Column(name = "secession_at", nullable = false)
    private LocalDateTime secessionAt;
}
