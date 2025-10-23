package com.eat.today.sns.query.config;

import com.eat.today.configure.security.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class NotesChainConfig {

    private final AuthenticationManager authenticationManager;

    /**
     * 쪽지(노트) 커맨드 체인
     * - 경로: /command/notes, /command/notes/**
     * - 기본 정책: 인증 필수
     */
    @Bean
    @Order(40) // 다른 command 체인들과 우선순위만 겹치지 않게 조정
    public SecurityFilterChain notesCommandChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/notes", "/notes/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 보내기(텍스트): /command/notes 또는 /command/notes/
                        .requestMatchers(HttpMethod.POST,
                                "/notes",
                                "/notes/").authenticated()

                        // 보내기(파일): /command/notes/files
                        .requestMatchers(HttpMethod.POST,
                                "/notes/files").authenticated()

                        // 받은/보낸함
                        .requestMatchers(HttpMethod.GET,
                                "/notes/inbox",
                                "/notes/outbox").authenticated()

                        // 읽음/답장
                        .requestMatchers(HttpMethod.POST,
                                "/notes/*/read",
                                "/notes/*/reply").authenticated()

                        // 개인함 삭제
                        .requestMatchers(HttpMethod.DELETE,
                                "/notes/*").authenticated()

                        // ★ 체인 내 다른 모든 요청도 인증 필수 (permitAll 금지)
                        .anyRequest().authenticated()
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
