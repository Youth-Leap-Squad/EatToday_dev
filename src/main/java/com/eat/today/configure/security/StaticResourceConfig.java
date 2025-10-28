package com.eat.today.configure.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {
    // 기존 StaticResourceConfig에서 base-dir 사용
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String base = "/Users/worms/EatToday_upload"; // 또는 @Value/props 주입
        registry.addResourceHandler("/reviews/**")
                .addResourceLocations("file:" + base + "/reviews/");
    }
}