package com.eat.today.member.query.service;

import com.eat.today.member.query.dto.FindMyLevelDTO;
import com.eat.today.member.query.dto.FindProfileDTO;
import com.eat.today.member.query.dto.ReportCheckDTO;
import com.eat.today.member.query.mapper.MemberMapper;
import com.eat.today.member.query.mapper.ReportMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
//
//-- 사용자가 이름, 비밀번호, 이메일, 닉네임 등을 입력하여 회원 가입
//-- 로그인된 사용자가 세션 종료.
//        -- 가입 시 입력한 전화번호를 통해 아이디를 확인.
//-- 등록된 ID와 핸드폰 번호로 비밀번호 찾기.
//-- 본인의 프로필, 활동 기록 등을 확인할 수 있다.
//-- 받은 포인트에 따른 등급을 확인할 수 있다.
//-- 신고 내용 확인
//-- 관리자가 별도 권한으로 로그인할 수 있다.

@Service
public class MemberServiceImpl implements  MemberService{

    private final SqlSession sqlSession;


    @Autowired
    public MemberServiceImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;

    }

    // 가입 시 입력한 전화번호를 통해 아이디를 확인.
    @Override
    public String getIdByPhone(String memberPhone) {
        return sqlSession.getMapper(MemberMapper.class)
                .selectIdByPhone(memberPhone);
    }

    // 등록된 전화번호로 비밀번호 재설정 안내.


    // 본인의 프로필, 활동 기록 등을 확인할 수 있다.
    @Override
    public FindProfileDTO findMyProfile(Integer memberNo) {
        return sqlSession.getMapper(MemberMapper.class)
                .findMyProfile(memberNo);
    }


    // 받은 포인트에 따른 등급을 확인할 수 있다.
    @Override
    public FindMyLevelDTO findMyLevel(Integer memberNo) {
        return sqlSession.getMapper(MemberMapper.class)
                .findMyLevel(memberNo);
    }

    // 신고 내용 확인
    @Override
    public List<ReportCheckDTO> checkReport() {
        return sqlSession.getMapper(ReportMapper.class)
                .checkReport();
    }

    @Override
    public UserDetails loadMemberByMemberName(String memberPhone) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }


    // 등록된 ID와 핸드폰 번호로 비밀번호 찾기.


    // 관리자가 별도 권한으로 로그인할 수 있다.
}
