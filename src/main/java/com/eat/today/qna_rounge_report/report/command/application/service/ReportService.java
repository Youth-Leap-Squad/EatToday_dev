package com.eat.today.qna_rounge_report.report.command.application.service;

import com.eat.today.qna_rounge_report.report.command.application.dto.CreateReportRequest;
import com.eat.today.qna_rounge_report.report.command.application.dto.CreateReportResponse;
import com.eat.today.qna_rounge_report.report.command.domain.aggregate.Report;
import com.eat.today.qna_rounge_report.report.command.domain.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {
    private final ReportRepository reportRepository;

    public CreateReportResponse create(CreateReportRequest req) {
        if (req.reporterId().equals(req.reportedId())) {
            throw new IllegalArgumentException("본인을 신고할 수 없습니다.");
        }
        Report saved = reportRepository.save(
                new Report(
                        req.reporterId(),
                        req.reportedId(),
                        req.title(),
                        req.content(),
                        req.reportDate(),
                        req.source()
                )
        );
        return CreateReportResponse.from(saved);
    }
}