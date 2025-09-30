package com.eat.today.sns.query.dto.photoReviewComment;

import lombok.Data;

@Data
public class PhotoReviewCommentDTO {
    private Integer prcNo;
    private Integer memberNo;
    private String  prcDetail;
    private String  prcAt;
    private Integer reviewNo;
    private Boolean prcDeleted;
}

