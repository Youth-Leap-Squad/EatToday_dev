package com.eat.today.qna_rounge_report.report.query.service;


import com.eat.today.qna_rounge_report.report.query.dto.ReportDTO;
import com.eat.today.qna_rounge_report.report.query.dto.ReportPageResponse;
import com.eat.today.qna_rounge_report.report.query.repository.ReportMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportSelectService {

    private final ReportMapper mapper;

    public ReportServiceImpl(ReportMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ReportPageResponse<ReportDTO> getAllPaged(int page, int size) {
        int offset = page * size;
        List<ReportDTO> list = mapper.selectAllPaged(offset, size);
        long total = mapper.countAll();
        return new ReportPageResponse<>(list, page, size, total);
    }
}