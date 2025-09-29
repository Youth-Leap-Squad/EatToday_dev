package com.eat.today.qna_rounge_report.report.command.infrastructure.persistence;

import com.eat.today.qna_rounge_report.report.command.domain.aggregate.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface SpringDataReportRepository extends JpaRepository<Report, Integer> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Report r set r.processed = true " +
            "where r.id = :id and r.processed = false")
    int markProcessedIfNot(@Param("id") Integer id);
}
