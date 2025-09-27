package com.eat.today.configure.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurity {

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(autz ->
                autz.requestMatchers(HttpMethod.GET,"/**").permitAll()      // 누구에게나 허용 (임시)
                        .anyRequest().authenticated()
        );

        return http.build();
    }
}
