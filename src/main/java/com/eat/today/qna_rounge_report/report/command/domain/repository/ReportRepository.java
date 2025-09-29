package com.eat.today.qna_rounge_report.report.command.domain.repository;

import com.eat.today.qna_rounge_report.report.command.domain.aggregate.Report;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface ReportRepository {
    Report save(Report report);
    Optional<Report> findById(Integer id);
    int markProcessedIfNot(Integer id);
}