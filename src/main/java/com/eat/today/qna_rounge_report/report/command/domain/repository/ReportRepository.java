package com.eat.today.qna_rounge_report.report.command.domain.repository;

import com.eat.today.qna_rounge_report.report.command.domain.aggregate.Report;

import java.util.Optional;

public interface ReportRepository {
    Report save(Report report);
    Optional<Report> findById(Integer id);
}
