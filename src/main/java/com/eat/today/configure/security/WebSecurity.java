package com.eat.today.configure.security;


import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

@Configuration
public class WebSecurity {

    private JwtAuthenticationProvider jwtAuthenticationProvider;

    public WebSecurity(JwtAuthenticationProvider jwtAuthenticationProvider) {
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
    }

    // 만든 provider bean으로 등록
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(jwtAuthenticationProvider));
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(autz ->
                autz.requestMatchers("/**").permitAll()      // 누구에게나 허용 (임시)
                        .anyRequest().authenticated()
        );

        http.addFilter(getAuthenticationFilter(authenticationManager()));
        
        // 로그아웃 설정
        http.logout(logout -> logout
                .logoutUrl("/logout")
                .addLogoutHandler((request, response, authentication) -> {
                    // 로그아웃 전에 사용자 정보를 request에 저장
                    System.out.println("로그아웃 핸들러 - authentication: " + authentication);
                    if (authentication != null) {
                        String userName = authentication.getName();
                        System.out.println("로그아웃 핸들러 - 사용자명: " + userName);
                        request.setAttribute("logoutUser", userName);
                    } else {
                        System.out.println("로그아웃 핸들러 - authentication이 null입니다");
                    }
                })
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.setStatus(HttpServletResponse.SC_OK);
                    
                    String memberPhone = (String) request.getAttribute("logoutUser");
                    if (memberPhone == null) {
                        memberPhone = "";
                    }
                    
                    String jsonResponse = "{\"message\":\"" + memberPhone + " 회원 로그아웃.\"}";
                    response.getWriter().write(jsonResponse);
                    response.getWriter().flush();
                })
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
        );

        return http.build();
    }

    // Filter을 등록하기 위해 사용하는 메소드
    private Filter getAuthenticationFilter(AuthenticationManager authenticationManager) {
        return new AuthenticationFilter(authenticationManager);
    }
}
