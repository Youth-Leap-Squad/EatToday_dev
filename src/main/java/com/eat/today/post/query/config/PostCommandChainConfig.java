package com.eat.today.post.query.config;

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
public class PostCommandChainConfig {

    private final AuthenticationManager authenticationManager;

    @Bean
    @Order(4) // 다른 체인보다 '먼저' 적용되도록 필요 시 숫자 조정
    public SecurityFilterChain postCommandChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/command/**") // /command/* 만 이 체인이 처리
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // ===== [가장 먼저] 술(alcohols) – ADMIN 전용 (모든 메서드) =====
                        .requestMatchers("/command/alcohols/**").hasRole("ADMIN")

                        // ===== 게시글/댓글/반응/즐겨찾기 – 로그인 필수 =====
                        .requestMatchers(HttpMethod.POST,   "/command/foods/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH,  "/command/foods/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/command/foods/**").authenticated()

                        .requestMatchers(HttpMethod.POST,   "/command/foods/*/comments/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH,  "/command/comments/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/command/comments/**").authenticated()

                        .requestMatchers(HttpMethod.POST,   "/command/foods/*/reactions/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH,  "/command/foods/*/reactions/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/command/foods/*/reactions/**").authenticated()

                        .requestMatchers(HttpMethod.POST,   "/command/bookmarks/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/command/bookmarks/**").authenticated()

                        // 그 외 /command/** 중 나머지(조회 등)는 허용
                        .anyRequest().permitAll()
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