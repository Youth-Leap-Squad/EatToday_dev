package com.eat.today.configure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalCorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 모든 요청 경로 허용
                        .allowedOrigins(
                                "http://localhost:5173", // Vite
                                "http://127.0.0.1:5173"  // Vite 대체 주소
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // 모든 HTTP 메서드 허용
                        .allowedHeaders("*") // 모든 헤더 허용
                        .exposedHeaders("Authorization", "Set-Cookie") // JWT 토큰, 쿠키 노출 허용
                        .allowCredentials(true) // 쿠키/인증정보 포함 허용
                        .maxAge(3600); // preflight 캐시 1시간
            }
        };
    }
}