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
public class PrChainConfig {

    private final AuthenticationManager authenticationManager;

    /**
     * 사진리뷰 본문/반응/파일: /command/photo-reviews/**
     */
    @Bean
    @Order(3)
    public SecurityFilterChain photoReviewsCommandChain(HttpSecurity http) throws Exception {
        http
                // ⛔ 오타 수정: "/./photo-reviews/**" -> "/command/photo-reviews/**"
                .securityMatcher("/command/photo-reviews", "/command/photo-reviews/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 본문 CUD
                        .requestMatchers(HttpMethod.POST,   "/command/photo-reviews", "/command/photo-reviews/").authenticated()
                        .requestMatchers(HttpMethod.PATCH,  "/command/photo-reviews/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/command/photo-reviews/**").authenticated()

                        // 반응(좋아요 등) — 필요 시 세부 경로로 좁히세요 (/command/photo-reviews/{id}/reactions 등)
                        .requestMatchers(HttpMethod.POST,   "/command/photo-reviews/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/command/photo-reviews/**").authenticated()

                        // 파일 업/삭제 — 필요 시 세부 경로로 좁히세요 (/command/photo-reviews/{id}/files 등)
                        .requestMatchers(HttpMethod.POST,   "/command/photo-reviews/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/command/photo-reviews/**").authenticated()

                        // 🔒 나머지도 전부 인증 필요 (permitAll 금지)
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

    /**
     * 사진리뷰 댓글(PRC): /command/prc/**
     */
    @Bean
    @Order(4) // 사진리뷰 체인 다음
    public SecurityFilterChain photoReviewCommentsCommandChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/command/prc", "/command/prc/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 삽입: /command/prc/reviews/{reviewNo}
                        .requestMatchers(HttpMethod.POST,   "/command/prc/reviews/**").authenticated()
                        // 수정: /command/prc/{prcNo}
                        .requestMatchers(HttpMethod.PATCH,  "/command/prc/*").authenticated()
                        // 삭제(soft/hard)
                        .requestMatchers(HttpMethod.DELETE, "/command/prc/*", "/command/prc/*/hard").authenticated()

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
