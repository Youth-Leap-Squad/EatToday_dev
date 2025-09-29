package com.eat.today.event.albti.query.service;

import com.eat.today.event.albti.query.dto.SelectAlbtiDTO;
import com.eat.today.event.albti.query.mapper.AlbtiMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlbtiService {

    private final SqlSession sqlSession;
    @Autowired
    public AlbtiService(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }
//    private AlbtiMapper albtiMapper;

//    private final SqlSession sqlSession;
//    @Autowired
//    public AlbtiService(SqlSession sqlSession) {
//        this.sqlSession = sqlSession;
//    }
//
//    public SelectAlbtiDTO selectAlbti(String member_no) {
//        return sqlSession.getMapper(AlbtiMapper.class)
//                .selectAlbti(member_no);
//    }
    public SelectAlbtiDTO selectAlbti(String member_no) {
        return sqlSession.getMapper(AlbtiMapper.class)
                .selectAlbti(member_no);
    }

//    public SelectAlbtiDTO getAlbitselect(String member_no) {
//        return albtiMapper.selectAlbti(member_no);
//    }
}
