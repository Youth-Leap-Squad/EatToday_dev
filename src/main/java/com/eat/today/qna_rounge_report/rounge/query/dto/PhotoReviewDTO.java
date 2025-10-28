package com.eat.today.qna_rounge_report.rounge.query.dto;

import com.eat.today.member.query.dto.RequestNameDTO;
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

    private RequestNameDTO member;

    private java.util.List<PrFileUploadDTO> files;
}
