package com.eat.today.qna_rounge_report.report.command.application.controller;

import com.eat.today.qna_rounge_report.report.command.application.service.ReportProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportProcessService reportProcessService;

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Void> confirm(@PathVariable Integer id) {
        reportProcessService.confirm(id);
        return ResponseEntity.ok().build();
    }
}
