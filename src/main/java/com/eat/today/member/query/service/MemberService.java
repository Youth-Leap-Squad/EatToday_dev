package com.eat.today.member.query.service;

import com.eat.today.member.query.dto.FindMyLevelDTO;
import com.eat.today.member.query.dto.FindProfileDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface MemberService extends UserDetailsService {


    // 가입 시 입력한 이메일을 통해 아이디를 확인.
    public String getIdByEmail(String memberEmail);

    // 등록된 전화번호로 비밀번호 재설정 안내.

    // 이메일로 프로필 조회
    public FindProfileDTO findMyProfileByEmail(String email);

    // 받은 포인트에 따른 등급을 확인할 수 있다.
    public FindMyLevelDTO findMyLevel(Integer memberNo);
    
    // member_no로 email 조회
    public String getEmailByNo(Integer memberNo);

    UserDetails loadMemberByMemberName(String memberEmail);

    // 등록된 ID와 핸드폰 번호로 비밀번호 찾기.
    // public String getPwByIdAndPhone(String memberPhone,)



    // 관리자가 별도 권한으로 로그인할 수 있다.


}
