package com.eat.today.post.command.domain.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FoodPostLikeId implements Serializable {

    @Column(name = "member_no")
    private Integer memberNo;

    @Column(name = "board_no")
    private Integer boardNo;
}
