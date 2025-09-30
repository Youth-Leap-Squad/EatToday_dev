package com.eat.today.sns.command.application.entity.photoReview;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="photo_review")
public class PhotoReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="review_no")
    private int reviewNo;

    @Column(name="board_no", nullable = false)
    private int boardNo;

    @Column(name="member_no", nullable = false)
    private int memberNo;

    @Column(name="prc_detail", nullable = false)
    private String prcDetail;

    @Column(name="prc_at", nullable = false)
    private String prcAt;

    @Column(name = "prc_deleted", nullable = false)
    private Boolean prcDeleted;
}
