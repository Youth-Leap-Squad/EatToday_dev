package com.eat.today.sns.command.application.entity.photoReview;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name="photo_review")
public class PhotoReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="review_no")
    private int reviewNo;

    @Column(name="board_no", nullable = false)
    private int boardNo;

    @Column(name="member_no", nullable = false)
    private int memberNo;

    @Column(name="review_title", nullable = false)
    private String reviewTitle;

    @Column(name="review_date", nullable = false)
    private String reviewDate;

    @Column(name = "review_content", nullable = false)
    private String reviewContent;

    @Column(name = "review_like", nullable = false)
    private int reviewLike;

    @PrePersist
    public void prePersist() {
        if (this.reviewDate == null) {
            this.reviewDate = LocalDate.now().toString();
        }
    }
}
