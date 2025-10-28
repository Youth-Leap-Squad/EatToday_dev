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
public class FollowChainConfig {

    private final AuthenticationManager authenticationManager;

    @Bean
    @Order(6) // PhotoReview(3), Post(4), DM(5) 뒤 — 필요시 간격 더 벌려도 됨
    public SecurityFilterChain followCommandChain(HttpSecurity http) throws Exception {
        http
                // 팔로우 관련 커맨드 엔드포인트만 매칭 (query용 GET은 별도 체인/컨트롤러에서)
                .securityMatcher(
                        "/./follows/**",
                        "/./follow-requests/**",
                        "/./blocks/**"
                )
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // ===== 팔로우/언팔로우 =====
                        .requestMatchers(HttpMethod.POST, "/./follows/**").authenticated()   // follow
                        .requestMatchers(HttpMethod.DELETE, "/./follows/**").authenticated()   // unfollow

                        // ===== 팔로우 요청(비공개 계정 승인 플로우) =====
                        .requestMatchers(HttpMethod.POST, "/./follow-requests/**").authenticated()  // 요청 보내기
                        .requestMatchers(HttpMethod.PATCH, "/./follow-requests/**").authenticated()  // 승인/거절 처리

                        // ===== 차단/차단 해제 =====
                        .requestMatchers(HttpMethod.POST, "/./blocks/**").authenticated()   // block
                        .requestMatchers(HttpMethod.DELETE, "/./blocks/**").authenticated()   // unblock

                        // 그 외는 허용 (조회 GET 등은 다른 체인에서 처리)
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
