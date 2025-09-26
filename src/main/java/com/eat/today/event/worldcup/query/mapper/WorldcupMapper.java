package com.eat.today.event.worldcup.query.mapper;

import com.eat.today.event.worldcup.query.dto.SelectWorldcupDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface WorldcupMapper {
    // 월드컵 랭킹 조회
    List<SelectWorldcupDTO> selectWorldcup(@Param("weekNo") String weekNo);
}
