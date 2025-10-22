package com.eat.today.configure.security;

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
public class ReportChainConfig {

    private final AuthenticationManager authenticationManager;

    @Bean
    @Order(1) // ← 특정 체인을 최우선 적용
    public SecurityFilterChain reportSecurityChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/reports/**", "/report/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1) 확정: ADMIN만
                        .requestMatchers(HttpMethod.POST, "/api/reports/*/confirm").hasRole("ADMIN")

                        // 2) 생성: MEMBER/ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/reports", "/api/reports/").hasAnyRole("USER","ADMIN")

                        // 3) 그 외 리포트 관련은 ADMIN만
                        .requestMatchers("/api/reports/**", "/report/**").hasRole("ADMIN")

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