package com.eat.today.post.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "food_comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_comment_no")
    private Integer foodCommentNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_no", nullable = false)
    private FoodPost post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;

    @Column(name = "fc_content", nullable = false)
    private String content;

    @Column(name = "fc_date", nullable = false)
    private String createdAt;

    @Column(name = "fc_udate")
    private String updatedAt;
}

