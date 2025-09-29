package com.eat.today.member.query.mapper;

import com.eat.today.member.query.dto.ReportCheckDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReportMapper {


    public List<ReportCheckDTO> checkReport();
}
