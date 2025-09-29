package com.eat.today.qna_rounge_report.report.command.infrastructure.persistence;

import com.eat.today.qna_rounge_report.report.command.domain.aggregate.Report;
import com.eat.today.qna_rounge_report.report.command.domain.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReportRepositoryJpaAdapter implements ReportRepository {
    private final SpringDataReportRepository delegate;

    @Override public Report save(Report report) {
        return delegate.save(report);
    }
    @Override public Optional<Report> findById(Integer id) {
        return delegate.findById(id);
    }

    @Override
    public int markProcessedIfNot(Integer id) {
        return delegate.markProcessedIfNot(id);
    }
}