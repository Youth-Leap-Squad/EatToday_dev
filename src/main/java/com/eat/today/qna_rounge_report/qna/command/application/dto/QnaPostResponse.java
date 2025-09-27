package com.eat.today.qna_rounge_report.qna.command.application.dto;

import com.eat.today.qna_rounge_report.qna.command.domain.aggregate.QnaPost;

public record QnaPostResponse(Integer id, Integer memberNo, String inquiryContent, String inquiryAt
) {
    public static QnaPostResponse from(QnaPost q) {
        return new QnaPostResponse(
                q.getId(),
                q.getMemberNo(),
                q.getInquiryContent(),
                q.getInquiryAt()
        );
    }
}