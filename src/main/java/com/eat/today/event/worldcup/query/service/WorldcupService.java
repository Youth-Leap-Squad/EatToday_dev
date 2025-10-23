package com.eat.today.event.worldcup.query.service;

import com.eat.today.event.worldcup.query.dto.SelectWorldcupDTO;
import com.eat.today.event.worldcup.query.mapper.WorldcupMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorldcupService {

    private final SqlSession sqlSession;
    @Autowired
    public WorldcupService(SqlSession sqlSession) {
        this.sqlSession = sqlSession;

    }

    public List<SelectWorldcupDTO> selectWorldcup(String alcoholNo, String weekNo) {
        return sqlSession.getMapper(WorldcupMapper.class)
                .selectWorldcup(alcoholNo,weekNo);
    }
}
