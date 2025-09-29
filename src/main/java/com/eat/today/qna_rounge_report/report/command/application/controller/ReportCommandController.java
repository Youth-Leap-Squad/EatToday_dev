package com.eat.today.qna_rounge_report.report.command.application.controller;

import com.eat.today.qna_rounge_report.report.command.application.dto.CreateReportRequest;
import com.eat.today.qna_rounge_report.report.command.application.dto.CreateReportResponse;
import com.eat.today.qna_rounge_report.report.command.application.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportCommandController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<CreateReportResponse> create(@RequestBody @Valid CreateReportRequest req) {
        CreateReportResponse created = reportService.create(req);
        return ResponseEntity.created(URI.create("/api/reports/" + created.id()))
                .body(created);
    }
}