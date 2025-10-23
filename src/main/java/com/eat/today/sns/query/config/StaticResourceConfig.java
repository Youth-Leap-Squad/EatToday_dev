package com.eat.today.sns.query.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Value("${app.upload.path:uploads}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String absolutePath = Paths.get(System.getProperty("user.dir"), uploadPath)
                .toFile()
                .getAbsolutePath();

        // 프로필 사진 정적 리소스 핸들러
        registry.addResourceHandler("/uploads/profile/**")
                .addResourceLocations("file:" + absolutePath + "/profile/");

        // 리뷰 이미지 정적 리소스 핸들러
        registry.addResourceHandler("/reviews/**")
                .addResourceLocations("file:" + absolutePath + "/reviews/");
    }
}
