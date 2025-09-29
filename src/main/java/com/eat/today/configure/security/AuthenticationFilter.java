package com.eat.today.configure.security;

import com.eat.today.member.command.application.dto.RequestLoginDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    // 우리가 만든 프로바이더를 알고 있는 매니져를 인지시킴
    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            RequestLoginDTO creds = new ObjectMapper().readValue(request.getInputStream(), RequestLoginDTO.class);
                return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(creds.getMemberPhone(), creds.getMemberPw(),new ArrayList<>())
            );


        }catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공 이후 spring security가 authentication 객체로 관리되며 넘어옴: {}",authResult);
        
        // 로그인 성공 응답 추가
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        
        String jsonResponse = "{\"message\":\"로그인 성공\",\"memberPhone\":\"" + authResult.getName() + "\"}";
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}
