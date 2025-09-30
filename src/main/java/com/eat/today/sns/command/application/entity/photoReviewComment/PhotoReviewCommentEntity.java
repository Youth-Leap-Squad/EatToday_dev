package com.eat.today.sns.command.application.entity.photoReviewComment;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="photo_review_comment")
public class PhotoReviewCommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="prc_no")
    private int prcNo;

    @Column(name="member_no", nullable = false)
    private int memberNo;

    @Column(name="prc_detail", nullable = false)
    private String prcDetail;

    @Column(name="prc_at", nullable = false)
    private String prcAt;

    @Column(name="review_no", nullable = false)
    private int reviewNo;

    @Column(name = "prc_deleted", nullable = false)
    private Boolean prcDeleted;
}
