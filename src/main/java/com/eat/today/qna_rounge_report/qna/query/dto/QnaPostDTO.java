package com.eat.today.qna_rounge_report.qna.query.dto;

import com.eat.today.member.query.dto.RequestNameDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class QnaPostDTO {
    private Integer qnaPostNo;
    private Integer memberNo;
    private String inquiryTitle;
    private String inquiryContent;
    private String inquiryAt;

    private RequestNameDTO questioner;
}
