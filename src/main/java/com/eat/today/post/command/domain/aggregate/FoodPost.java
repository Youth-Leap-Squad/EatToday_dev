package com.eat.today.post.command.domain.aggregate;

import com.eat.today.common.jpa.BooleanTFConverter;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="food_post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="board_no")
    private Integer boardNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="alcohol_no")
    private Alcohol alcohol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_no")
    private Member member;

    @Column(name="board_title")
    private String  boardTitle;

    @Column(name="board_content", columnDefinition="TEXT")
    private String  boardContent;

    @Column(name="food_explain",  columnDefinition="TEXT")
    private String  foodExplain;

    @Column(name="food_picture")
    private String  foodPicture;

    @Column(name="board_date")
    private String  boardDate;

    @Column(name="board_seq")
    private Integer boardSeq;


    @Convert(converter = BooleanTFConverter.class)
    @Column(name = "confirmed_yn", columnDefinition = "CHAR(1)") // DB가 CHAR(1) 'T'/'F' 라면 명시
    private Boolean confirmedYn;

    @Column(name="likes_no_1")
    private Integer likeNo1;

    @Column(name="likes_no_2")
    private Integer likeNo2;

    @Column(name="likes_no_3")
    private Integer likeNo3;

    @Column(name="likes_no_4")
    private Integer likeNo4;

    public void update(String title, String content, String explain, String picture) {
        if (title   != null) this.boardTitle   = title;
        if (content != null) this.boardContent = content;
        if (explain != null) this.foodExplain  = explain;
        if (picture != null) this.foodPicture  = picture;
    }

    public void increaseLike(LikesType t){
        switch (t){
            case LIKE_1 -> this.likeNo1 = (likeNo1==null?0:likeNo1) + 1;
            case LIKE_2 -> this.likeNo2 = (likeNo2==null?0:likeNo2) + 1;
            case LIKE_3 -> this.likeNo3 = (likeNo3==null?0:likeNo3) + 1;
            case LIKE_4 -> this.likeNo4 = (likeNo4==null?0:likeNo4) + 1;
        }
    }
    public void decreaseLike(LikesType t){
        switch (t){
            case LIKE_1 -> this.likeNo1 = Math.max(0,(likeNo1==null?0:likeNo1)-1);
            case LIKE_2 -> this.likeNo2 = Math.max(0,(likeNo2==null?0:likeNo2)-1);
            case LIKE_3 -> this.likeNo3 = Math.max(0,(likeNo3==null?0:likeNo3)-1);
            case LIKE_4 -> this.likeNo4 = Math.max(0,(likeNo4==null?0:likeNo4)-1);
        }
    }
}
