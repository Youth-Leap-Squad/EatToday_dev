package com.eat.today.sns.query.dto.photoReviewComment;

import lombok.Data;

@Data
public class PhotoReviewCommentDTO {
    private Integer prcNo;       // prc_no
    private Integer memberNo;    // member_no
    private String  prcDetail;   // prc_detail
    private String  prcAt;       // prc_at (가능하면 LocalDateTime 권장)
    private Integer reviewNo;    // review_no
    private Boolean prcDeleted;  // prc_deleted
}

