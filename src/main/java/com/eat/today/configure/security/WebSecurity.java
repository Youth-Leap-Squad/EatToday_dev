package com.eat.today.configure.security;

import com.eat.today.member.command.application.service.MemberPointService;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurity {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final JwtTokenService jwtTokenService;
    private final MemberPointService memberPointService;

    public WebSecurity(JwtAuthenticationProvider jwtAuthenticationProvider,
                       JwtTokenService jwtTokenService,
                       MemberPointService memberPointService) {
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.jwtTokenService = jwtTokenService;
        this.memberPointService = memberPointService;
    }

    /** AuthenticationManager: 커스텀 Provider만 등록 */
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(jwtAuthenticationProvider));
    }

    /** JWT 인가 필터 (빈으로 등록해서 재사용) */
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        return new JwtAuthorizationFilter(authenticationManager);
    }

    /** 메인 보안 체인 */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthorizationFilter jwtAuthorizationFilter) throws Exception {
        http.csrf(csrf -> csrf.disable());

        // ✅ 접근 정책: /command/** 는 인증 필요, 조회성/정적 리소스는 허용
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/command/**").authenticated()
                .requestMatchers("/query/**", "/foods/**").permitAll()
                .requestMatchers("/", "/error", "/actuator/**", "/images/**", "/uploads/**").permitAll()
                .anyRequest().permitAll()
        );

        // ✅ 필터 체인: (1) 로그인용 커스텀 AuthenticationFilter, (2) JWT 인가 필터
        //    - JWT 인가 필터는 UsernamePasswordAuthenticationFilter 이전에 배치
        http.addFilter(loginAuthenticationFilter(authenticationManager()));
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        // ✅ 로그아웃 응답 커스터마이즈
        http.logout(logout -> logout
                .logoutUrl("/logout")
                .addLogoutHandler((request, response, authentication) -> {
                    if (authentication != null) {
                        request.setAttribute("logoutUser", authentication.getName());
                    }
                })
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.setStatus(HttpServletResponse.SC_OK);
                    String memberPhone = (String) request.getAttribute("logoutUser");
                    if (memberPhone == null) memberPhone = "";
                    String jsonResponse = "{\"message\":\"" + memberPhone + " 회원 로그아웃.\"}";
                    response.getWriter().write(jsonResponse);
                    response.getWriter().flush();
                })
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
        );

        return http.build();
    }

    /** 로그인 처리용 커스텀 필터 등록 */
    private Filter loginAuthenticationFilter(AuthenticationManager authenticationManager) {
        return new AuthenticationFilter(authenticationManager, jwtTokenService, memberPointService);
    }
}
