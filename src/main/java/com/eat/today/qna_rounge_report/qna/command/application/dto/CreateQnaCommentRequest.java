package com.eat.today.qna_rounge_report.qna.command.application.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateQnaCommentRequest(
        @NotNull Integer qnaPostNo,
        @NotNull Integer commentMemberNo,
        @NotBlank String comment,
        @NotBlank String createdAt
) {}