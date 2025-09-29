package com.eat.today.qna_rounge_report.report.command.infrastructure.persistence;

import com.eat.today.qna_rounge_report.report.command.domain.aggregate.Report;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataReportRepository extends JpaRepository<Report, Integer> {

}
