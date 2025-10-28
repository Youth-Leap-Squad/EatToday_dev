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

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String projectRoot = System.getProperty("user.dir");
        
        // 프로필 이미지: /uploads/profile/** → uploads/profile/파일명
        String profilePath = Paths.get(projectRoot, "uploads/profile")
                .toFile()
                .getAbsolutePath();
        
        // 일반 업로드: /uploads/** → uploads/폴더/파일명
        String generalUploadPath = Paths.get(projectRoot, "uploads")
                .toFile()
                .getAbsolutePath();
        
        // 포스트 이미지 정적 리소스 핸들러 (일반 업로드)
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + generalUploadPath + "/");
        
        // 프로필 사진 정적 리소스 핸들러
        registry.addResourceHandler("/uploads/profile/**")
                .addResourceLocations("file:" + profilePath + "/");
    }
}
