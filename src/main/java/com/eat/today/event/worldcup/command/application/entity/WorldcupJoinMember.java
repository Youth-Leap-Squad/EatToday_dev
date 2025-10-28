package com.eat.today.event.worldcup.command.application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name="worldcup_join_member")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class WorldcupJoinMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "worldcup_join_member_no", nullable = false)
    private int worldcupJoinMemberNo;

    @Column(name = "worldcup_no", nullable = false)
    private int worldcupNo;

    @Column(name = "member_no", nullable = false)
    private int memberNo;

    @Column(name = "alcohol_no", nullable = false)
    private int alcoholId;

    @Column(name = "participated_at")
    private LocalDate participatedAt;

}
