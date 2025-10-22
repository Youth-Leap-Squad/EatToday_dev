package com.eat.today.qna_rounge_report.report.query.controller;


import com.eat.today.qna_rounge_report.report.query.dto.ReportDTO;
import com.eat.today.qna_rounge_report.report.query.dto.ReportPageResponse;
import com.eat.today.qna_rounge_report.report.query.service.ReportSelectService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
public class ReportSelectController {

    private final ReportSelectService service;

    public ReportSelectController(ReportSelectService service) {
        this.service = service;
    }

    @GetMapping
    public ReportPageResponse<ReportDTO> getAllReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.getAllPaged(page, size);
    }
}