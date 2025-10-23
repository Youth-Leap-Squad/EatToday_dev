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
public class WorldcupChainConfig {

    private final AuthenticationManager authenticationManager;

    @Bean
    @Order(6) // Albti보다 뒤, QnA/Photo보다 뒤에 올 수 있도록 적절한 순서
    public SecurityFilterChain worldcupChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/worldcup/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // ✅ 로그인 사용자만 가능한 기능
                        .requestMatchers(
                                "/worldcup/join",
                                "/worldcup/repick"
                        ).authenticated()

                        // ✅ 랭킹 조회는 공개로 설정 (필요시 authenticated로 변경 가능)
                        .requestMatchers("/worldcup/getworldcupresult").permitAll()

                        // ✅ 기타 worldcup API는 모두 허용
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