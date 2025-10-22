package com.eat.today.configure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class QnaChainConfig {

    private final AuthenticationManager authenticationManager;

    @Bean
    @Order(2)
    public SecurityFilterChain qnaChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/qna/**", "/api/qna-posts/**", "/api/qna-comments/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 댓글 생성/수정/삭제: ADMIN 롤 필요 (ROLE_ADMIN을 찾음)
                        .requestMatchers("/api/qna-comments", "/api/qna-comments/**").hasRole("ADMIN")
                        // 그 외 QnA 관련 경로는 인증만
                        .requestMatchers("/qna/**", "/api/qna-posts/**").authenticated()
                        // QnA 체인에 들어왔는데 위에 안 걸리면 거부(안전)
                        .anyRequest().denyAll()
                )
                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint((req, res, ex) -> {
                            res.setStatus(401);
                            res.setContentType("application/json;charset=UTF-8");
                            res.getWriter().write("{\"error\":\"unauthorized\"}");
                        })
                        .accessDeniedHandler((req, res, ex) -> {
                            res.setStatus(403);
                            res.setContentType("application/json;charset=UTF-8");
                            res.getWriter().write("{\"error\":\"forbidden\"}");
                        })
                )
                .addFilterBefore(new JwtAuthorizationFilter(authenticationManager),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}