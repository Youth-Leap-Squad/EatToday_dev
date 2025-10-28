package com.eat.today.sns.query.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Configuration(proxyBeanMethods = false)
public class SnsStaticResourceConfig implements WebMvcConfigurer {

    /** 존재하면 사용, 없으면 자동으로 프론트 public 폴더 매핑 (기본값: 빈 문자열) */
    @Value("${app.upload.base-dir:}")
    private String uploadBaseDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        var cache = CacheControl.maxAge(30, TimeUnit.DAYS).cachePublic();

        if (StringUtils.hasText(uploadBaseDir)) {
            // ── ① yml 명시 경로: 백엔드가 직접 저장/서빙할 때
            Path base = Paths.get(uploadBaseDir).toAbsolutePath().normalize();
            String baseUri = ensureSlash(base.toUri().toString()); // e.g. file:/.../uploads/

            // /files/** → {baseDir}/**
            registry.addResourceHandler("/files/**")
                    .addResourceLocations(baseUri)
                    .setCacheControl(cache)
                    .resourceChain(true);

            // /photo_review/** → {baseDir}/**  (원하는 별칭)
            registry.addResourceHandler("/photo_review/**")
                    .addResourceLocations(baseUri)
                    .setCacheControl(cache)
                    .resourceChain(true);

            // (선택) 하위호환: /reviews/** → {baseDir}/reviews/**
            registry.addResourceHandler("/reviews/**")
                    .addResourceLocations(baseUri + "reviews/")
                    .setCacheControl(cache)
                    .resourceChain(true);

        } else {
            // ── ② yml 없이: 프론트 public 폴더 자동 매핑
            // 현재 프로젝트 구조가 두 가지 케이스가 있음:
            //  A) ../eatToday_front/eatToday_front/public
            //  B) ../eatToday_front/public
            Path backendRoot = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();

            Path nestedPublic = backendRoot.getParent()
                    .resolve("EatToday_Front")
                    .resolve("eatToday_front")
                    .resolve("public")
                    .normalize();

            Path singlePublic = backendRoot.getParent()
                    .resolve("EatToday_Front")
                    .resolve("public")
                    .normalize();

            Path frontPublic = Files.isDirectory(nestedPublic) ? nestedPublic : singlePublic;
            String publicUri = ensureSlash(frontPublic.toUri().toString()); // file:/.../public/

            // PhotoReview: public/images/photo_review/*
            registry.addResourceHandler("/images/photo_review/**")
                    .addResourceLocations(publicUri + "images/photo_review/")
                    .setCacheControl(cache)
                    .resourceChain(true);

            // Note(쪽지): public/files/notes/*
            registry.addResourceHandler("/files/notes/**")
                    .addResourceLocations(publicUri + "files/notes/")
                    .setCacheControl(cache)
                    .resourceChain(true);

            // 범용 /files/** → public/**
            registry.addResourceHandler("/files/**")
                    .addResourceLocations(publicUri)
                    .setCacheControl(cache)
                    .resourceChain(true);

            // (선택) 하위호환
            registry.addResourceHandler("/reviews/**")
                    .addResourceLocations(publicUri + "reviews/")
                    .setCacheControl(cache)
                    .resourceChain(true);

            // (선택) 별칭 유지
            registry.addResourceHandler("/photo_review/**")
                    .addResourceLocations(publicUri + "images/photo_review/")
                    .setCacheControl(cache)
                    .resourceChain(true);
        }
    }

    private static String ensureSlash(String uri) {
        return uri.endsWith("/") ? uri : uri + "/";
    }
}