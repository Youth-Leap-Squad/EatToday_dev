package com.eat.today.post.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @Column(name = "member_no")
    private Integer memberNo;

    @Column(name = "member_id")
    private String memberId;

    /** 연관키만 필요한 곳에서 사용 (insert/select 안 하고 FK로만 참조할 때) */
    public static Member onlyId(Integer id) {
        return Member.builder().memberNo(id).build();
    }
}
