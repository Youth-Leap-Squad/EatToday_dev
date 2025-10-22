package com.eat.today.qna_rounge_report.qna.query.dto;

import com.eat.today.member.query.dto.RequestNameDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QnaCommentDTO {
    private Integer commentNo;
    private Integer qnaPostNo;
    private Integer commentMemberNo;
    private String commentContent;
    private String commentAt;

    // 답변자
    private RequestNameDTO answerer;

    // 문의자
    private RequestNameDTO questioner;
}
