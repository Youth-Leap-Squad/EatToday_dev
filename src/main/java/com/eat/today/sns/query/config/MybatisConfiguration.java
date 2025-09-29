package com.eat.today.sns.query.config;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@MapperScan(basePackages = "com.eat.today.sns.query", annotationClass = Mapper.class)
@EnableWebSecurity
public class MybatisConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 끄기
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/prc/**").permitAll() // 댓글 API는 모두 접근 가능
                        .anyRequest().permitAll() // 테스트니까 전체 허용
                );

        return http.build();
    }
}
