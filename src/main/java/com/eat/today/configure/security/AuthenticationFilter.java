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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtTokenService jwtTokenService;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                JwtTokenService jwtTokenService) {
        super(authenticationManager);
        this.jwtTokenService = jwtTokenService;
        setFilterProcessesUrl("/login"); // POST /login
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            RequestLoginDTO creds = new ObjectMapper().readValue(request.getInputStream(), RequestLoginDTO.class);

            // 이메일/비밀번호로 통일 (DTO 필드명은 memberEmail, memberPw)
            String email = creds.getMemberEmail() == null ? "" : creds.getMemberEmail().trim();
            String pw    = creds.getMemberPw() == null ? "" : creds.getMemberPw();

            UsernamePasswordAuthenticationToken authRequest =
                    new UsernamePasswordAuthenticationToken(email, pw, new ArrayList<>());

            return this.getAuthenticationManager().authenticate(authRequest);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공: principal={}, authorities={}",
                authResult.getName(), authResult.getAuthorities());

        // JWT 발급
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        String username = authResult.getName(); // 여기 값이 로그인 식별자(이메일)여야 함
        String token = jwtTokenService.issueToken(username, authorities, Duration.ofHours(12));


        // 헤더/바디로 전달
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Authorization", "Bearer " + token);

        // 응답 바디 (원하면 refreshToken 등 추가)
        new ObjectMapper().writeValue(response.getWriter(),
                Map.of(
                        "message", "로그인 성공",
                        "memberEmail", authResult.getName(),
                        "tokenType", "Bearer",
                        "accessToken", token
                )
        );
        response.getWriter().flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        log.warn("로그인 실패: {}", failed.getMessage());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        new ObjectMapper().writeValue(response.getWriter(),
                Map.of("error", "unauthorized", "message", failed.getMessage()));
        response.getWriter().flush();
    }
}
