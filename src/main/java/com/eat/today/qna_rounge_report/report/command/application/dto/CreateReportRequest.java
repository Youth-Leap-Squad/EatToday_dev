package com.eat.today.qna_rounge_report.report.command.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateReportRequest(

        @NotNull
        Integer reporterId,   // 신고한 회원 번호

        @NotNull
        Integer reportedId,   // 신고 당한 회원 번호

        @NotBlank
        String title,

        @NotBlank
        String content,

        @NotBlank
        String reportDate,

        @NotBlank
        String source
) {}