package com.eat.today.configure.security;

import com.eat.today.member.command.application.service.CommandMemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityBeansConfig {

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(JwtTokenService jwtTokenService,
                                                         CommandMemberService commandMemberService) {
        return new JwtAuthorizationFilter(jwtTokenService, commandMemberService);
    }
}
