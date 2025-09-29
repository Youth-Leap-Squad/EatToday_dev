package com.eat.today.qna_rounge_report.rounge.query.dto;

import com.eat.today.member.query.dto.RequestLoungeDTO;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PhotoReviewDTO {
    private String reviewTitle;
    private String reviewDate;
    private String reviewContent;
    private Integer reviewLike;

    private RequestLoungeDTO member;
}
