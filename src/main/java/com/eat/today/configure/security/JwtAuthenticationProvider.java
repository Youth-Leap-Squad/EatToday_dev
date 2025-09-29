package com.eat.today.configure.security;

import com.eat.today.member.command.application.service.CommandMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {


    private final CommandMemberService commandMemberService;
    private final PasswordEncoder passwordEncoder; // 평문과 암호화 비교 위한 도구

    @Autowired
    public JwtAuthenticationProvider(CommandMemberService commandMemberService,
                                     PasswordEncoder passwordEncoder) {
        this.commandMemberService = commandMemberService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // 사용자가 로그인 시 입력한 값.
        String memberPhone =  authentication.getName();
        String memberPw = authentication.getCredentials().toString();

        // DB에 있는 해당 회원의 정보
        UserDetails userDetails = commandMemberService.loadUserByUsername(memberPhone);
        if (userDetails == null) {
            // 규약 위반 방지: 명시적으로 예외
            throw new UsernameNotFoundException("No user: " + memberPhone);
        }
        //로그인 실패시
        if(!passwordEncoder.matches(memberPw,userDetails.getPassword())){
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        // 로그인 성공시  Token 생성

        return new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
