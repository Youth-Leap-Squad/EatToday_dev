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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class PrChainConfig {

    private final AuthenticationManager authenticationManager;

    /** ✅ 공용 CORS 설정 Bean */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // 프론트 주소 허용
        config.addAllowedOriginPattern("http://localhost:5173");
        config.addAllowedOriginPattern("http://127.0.0.1:5173");
        // 허용 메서드
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        // 헤더 허용
        config.setAllowedHeaders(List.of("*"));
        // JWT 토큰 응답 헤더 노출
        config.setExposedHeaders(List.of("Authorization", "Set-Cookie"));
        // 쿠키 / 인증정보 포함 허용
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /** 📸 사진 리뷰 본문/파일/반응 */
    @Bean
    @Order(3)
    public SecurityFilterChain photoReviewsCommandChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/command/photo-reviews", "/command/photo-reviews/**")
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ CORS 활성화
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // ✅ OPTIONS 요청은 항상 허용 (Preflight)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ✅ 인증 필요한 요청들
                        .requestMatchers(HttpMethod.POST, "/command/photo-reviews", "/command/photo-reviews/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/command/photo-reviews/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/command/photo-reviews/**").authenticated()

                        // ✅ 나머지는 기본적으로 허용하지 않음
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
                // ✅ JWT 필터 등록 (Security 인증 필터보다 먼저)
                .addFilterBefore(new JwtAuthorizationFilter(authenticationManager),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /** 💬 사진리뷰 댓글(PRC) */
    @Bean
    @Order(4)
    public SecurityFilterChain photoReviewCommentsCommandChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/command/prc", "/command/prc/**")
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ CORS 활성화
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // ✅ OPTIONS 요청 무조건 허용
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ✅ 댓글 관련 요청 인증
                        .requestMatchers(HttpMethod.POST, "/command/prc/reviews/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/command/prc/*").authenticated()
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
                // ✅ JWT 필터 등록
                .addFilterBefore(new JwtAuthorizationFilter(authenticationManager),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}