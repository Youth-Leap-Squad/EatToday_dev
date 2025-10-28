package com.eat.today.member.command.application.service;

import com.eat.today.member.command.application.dto.CommandMemberDTO;
import com.eat.today.member.command.application.dto.CommandUpdateMemberDTO;
import com.eat.today.member.command.domain.aggregate.MemberEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CommandMemberService {

    void registMember(CommandMemberDTO commandMemberDTO);
    
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;


    // 사용자는 회원명, 비밀번호 등 개인 정보 수정을 할 수 있다.
    void updatemember(CommandUpdateMemberDTO commandUpdateMemberDTO);


    //사용자는 회원 탈퇴를 할 수 있다.
    public void withdraw(String memberEmail, String rawPw);
    
    // 비밀번호 검증
    boolean verifyPassword(String memberEmail, String rawPassword);
    
    // 비밀번호 변경
    void changePassword(String memberEmail, String currentPassword, String newPassword);







}
