package com.eat.today.qna_rounge_report.report.query.repository;


import com.eat.today.qna_rounge_report.report.query.dto.ReportDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface ReportMapper {
    List<ReportDTO> selectAllPaged(@Param("offset") int offset, @Param("size") int size);
    long countAll();
}
