package com.eat.today.sns.query.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Configuration
public class SnsStaticResourceConfig implements WebMvcConfigurer {

    // application.yml의 app.upload.base-dir 주입
    @Value("${app.upload.base-dir}")
    private String uploadBaseDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 설정값(절대경로)을 파일 URI로 변환 (운영체제 구분/공백/역슬래시 안전)
        String baseUri = Paths.get(uploadBaseDir)
                .toAbsolutePath()
                .normalize()
                .toUri()
                .toString(); // 예: file:/Users/you/EatToday_uploads/

        // 1) 통합 핸들러: /files/** → app.upload.base-dir/ 이하를 그대로 노출
        registry.addResourceHandler("/files/**")
                .addResourceLocations(baseUri)  // 끝에 슬래시 포함되는 URI
                .setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS).cachePublic())
                .resourceChain(true);

        // 2) 하위 호환: 기존의 /reviews/** 경로도 유지하고 싶다면(선택)
        registry.addResourceHandler("/reviews/**")
                .addResourceLocations(baseUri + "reviews/") // = ${base-dir}/reviews/
                .setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS).cachePublic())
                .resourceChain(true);

        // 3) 노트 첨부(URL을 /files/notes/{noteId}/{file}로 쓰려면 별도 추가 불필요)
        //   NoteCommandService에서 저장한 실제 경로가 ${base-dir}/notes/… 라면
        //   클라이언트는 /files/notes/{noteId}/{storedName} 로 접근 가능
    }
}
