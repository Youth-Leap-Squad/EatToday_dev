package com.eat.today.member.command.application.service;

import com.eat.today.member.command.application.dto.CommandMemberDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CommandMemberService {

    void registMember(CommandMemberDTO commandMemberDTO);
    
    UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException;



}
