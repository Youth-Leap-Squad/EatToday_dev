package com.eat.today.member.query.service;

import com.eat.today.member.query.dto.MemberDTO;
import com.eat.today.member.query.mapper.MemberMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final SqlSession sqlSession;
    @Autowired
    public MemberService(SqlSession sqlSession) {
        this.sqlSession = sqlSession;

    }

    // 가입 시 입력한 전화번호를 통해 아이디를 확인.
    // 단일열, 단일행 조회

    public String getIdByPhone(String memberPhone) {

        return sqlSession.getMapper(MemberMapper.class)
                .selectIdByPhone(memberPhone);
    }

    // 등록된 전화번호로 비밀번호 재설정 안내.


    // 본인의 프로필, 활동 기록 등을 확인할 수 있다.
    // -> 단일열, 다중행 조회

    public MemberDTO findMyProfile(String memberNo) {
        return sqlSession.getMapper(MemberMapper.class)
                .findMyProfile(memberNo);
    }

    // 받은 포인트에 따른 등급을 확인할 수 있다.

    // 신고 내용 확인

    // 관리자가 별도 권한으로 로그인할 수 있다.
}
