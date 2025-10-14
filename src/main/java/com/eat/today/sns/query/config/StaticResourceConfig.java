package com.eat.today.sns.query.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = Paths.get(System.getProperty("user.dir"), "uploads")
                .toFile()
                .getAbsolutePath();
        registry.addResourceHandler("/reviews/**")
                .addResourceLocations("file:" + uploadPath + "/reviews/");
    }
}
