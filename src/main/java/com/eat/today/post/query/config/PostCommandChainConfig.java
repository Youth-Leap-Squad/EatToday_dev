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
    @Order(4) // PhotoReviewChain(3) 뒤에 오도록 순서 지정 (프로젝트 상황에 맞게 조정)
    public SecurityFilterChain postCommandChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/command/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // ===== 게시글(foods) – 로그인 필수 =====
                        .requestMatchers(HttpMethod.POST,   "/command/foods/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH,  "/command/foods/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/command/foods/**").authenticated()

                        // (관리자 전용) 승인
                        .requestMatchers(HttpMethod.PATCH, "/command/foods/*/approve").hasRole("ADMIN")

                        // ===== 댓글 – 로그인 필수 =====
                        .requestMatchers(HttpMethod.POST,   "/command/foods/*/comments/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH,  "/command/comments/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/command/comments/**").authenticated()

                        // ===== 반응(리액션) – 로그인 필수 =====
                        .requestMatchers(HttpMethod.POST,   "/command/foods/*/reactions/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH,  "/command/foods/*/reactions/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/command/foods/*/reactions/**").authenticated()

                        // ===== 즐겨찾기 – 로그인 필수 =====
                        .requestMatchers(HttpMethod.POST,   "/command/bookmarks/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/command/bookmarks/**").authenticated()

                        // 그 외는 허용 (ex. 조회용 GET은 다른 조회 컨트롤러에서 처리)
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