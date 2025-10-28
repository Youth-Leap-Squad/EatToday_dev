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
public class AlbtiChainConfig {

    private final AuthenticationManager authenticationManager;

    @Bean
    @Order(5)
    public SecurityFilterChain albtiChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/albti/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // ✅ 관리자용
                        .requestMatchers("/albti/survey/add").hasRole("ADMIN")

                        // ✅ 로그인 사용자 전용 기능
                        .requestMatchers(
                                "/albti/answer/again",
                                "/albti/member/add-bulk",
                                "/albti/member/add",
                                "/albti/getalbtiresult",
                                "/albti/survey/list"
                        ).authenticated()

//                        .requestMatchers(
//                                "/albti/member/add-bulk",
//                                "/albti/survey/list",
//                                "/albti/getalbtiresult"
//                        ).permitAll()

                        // ✅ 그 외 모든 albti 요청은 일단 허용
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