package com.eat.today.qna_rounge_report.report.command.application.dto;


import com.eat.today.qna_rounge_report.report.command.domain.aggregate.Report;

public record CreateReportResponse(Integer id, Integer reporterId, Integer reportedId, String title, String content, Boolean processed, String reportDate, String source) {
    public static CreateReportResponse from(Report r) {
        return new CreateReportResponse(
                r.getId(),
                r.getReporterId(),
                r.getReportedId(),
                r.getTitle(),
                r.getContent(),
                r.getProcessed(),
                r.getReportDate(),
                r.getSource()
        );
    }
}