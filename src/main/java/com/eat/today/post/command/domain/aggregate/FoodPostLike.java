package com.eat.today.post.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="food_post_likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodPostLike {

    @EmbeddedId
    private FoodPostLikeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("boardNo")
    @JoinColumn(name="board_no")
    private FoodPost post;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memberNo")
    @JoinColumn(name="member_no")
    private Member member;

    @Convert(converter = LikesTypeConverter.class)
    @Column(name = "likes_type", nullable = false,
            columnDefinition = "ENUM('술술 들어가요','참신해요','맛없어요','궁금해요')")
    private LikesType likesType;
}
