package com.eat.today.sns.query.dto.photoReview;

import com.eat.today.qna_rounge_report.rounge.query.dto.PrFileUploadDTO;
import lombok.Data;

import java.util.List;

@Data
public class PRDTO {
    private Integer reviewNo;
    private Integer boardNo;
    private Integer memberNo;
    private String reviewTitle;
    private String reviewContent;
    private String reviewDate;
    private Integer reviewLike;

    // 첨부파일 목록
    private List<PrFileUploadDTO> files;
}
