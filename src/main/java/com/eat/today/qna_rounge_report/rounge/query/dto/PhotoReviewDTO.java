package com.eat.today.qna_rounge_report.rounge.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PhotoReviewDTO {
    private Integer reviewNo;
    private Integer boardNo;
    private Integer memberNo;
    private String reviewTitle;
    private String reviewDate;
    private String reviewContent;
    private Integer reviewLike;
}
