package com.eat.today.qna_rounge_report.report.query.dto;

import com.eat.today.member.query.dto.RequestNameDTO;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReportDTO {
    private Integer reportNo;
    private Integer memberNo;     // 피신고자 회원번호
    private Integer memberNo2;    // 신고자 회원번호
    private String reportTitle;
    private String reportContent;
    private Boolean reportYn;
    private String reportDate;
    private String reportSource;

    private RequestNameDTO accused;   // 피신고자 이름
    private RequestNameDTO reporter;  // 신고자 이름
}