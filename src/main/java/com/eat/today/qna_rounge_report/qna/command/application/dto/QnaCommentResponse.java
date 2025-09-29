package com.eat.today.qna_rounge_report.qna.command.application.dto;

import com.eat.today.qna_rounge_report.qna.command.domain.aggregate.QnaComment;

public record QnaCommentResponse(
        Integer id,
        Integer qnaPostNo,
        Integer commentMemberNo,
        String commentContent,
        String commentAt
) {
    public static QnaCommentResponse from(QnaComment comment) {
        return new QnaCommentResponse(
                comment.getId(),
                comment.getQnaPostNo(),
                comment.getCommentMemberNo(),
                comment.getCommentContent(),
                comment.getCommentAt()
        );
    }
}
