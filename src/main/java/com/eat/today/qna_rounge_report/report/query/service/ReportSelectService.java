package com.eat.today.qna_rounge_report.report.query.service;


import com.eat.today.qna_rounge_report.report.query.dto.ReportDTO;
import com.eat.today.qna_rounge_report.report.query.dto.ReportPageResponse;

public interface ReportSelectService {
    ReportPageResponse<ReportDTO> getAllPaged(int page, int size);
}