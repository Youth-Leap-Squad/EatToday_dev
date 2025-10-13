package com.eat.today.sns.command.domain.service.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
public class PhotoReviewFileStorage {

    @Value("${app.upload.root:uploads}")
    private String uploadRoot;

    private static final long MAX_SIZE = 10 * 1024 * 1024;
    private static final Set<String> ALLOWED = Set.of(
            "image/jpeg","image/png","image/gif","image/webp","image/heic","image/heif"
    );

    /** 저장 (로컬) */
    public Stored store(int reviewNo, MultipartFile mf) throws IOException {
        if (mf.getSize() > MAX_SIZE) throw new IllegalArgumentException("최대 10MB까지 허용: " + mf.getOriginalFilename());
        String ct = Optional.ofNullable(mf.getContentType()).orElse("application/octet-stream");
        if (!ALLOWED.contains(ct)) throw new IllegalArgumentException("이미지 형식만 허용: " + ct);

        String datePath = DateTimeFormatter.ofPattern("yyyy/MM").format(LocalDateTime.now());
        Path dir = Paths.get(uploadRoot, "reviews", String.valueOf(reviewNo), datePath);
        Files.createDirectories(dir);

        String original = mf.getOriginalFilename();
        String ext = (original != null && original.lastIndexOf('.') >= 0) ? original.substring(original.lastIndexOf('.')) : "";
        String rename = UUID.randomUUID().toString().replace("-", "") + ext;

        Path target = dir.resolve(rename);
        try (var in = mf.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }

        // 정적 매핑 전제: /static/** -> {uploadRoot}/**
        String publicPath = String.join("/", "static", "reviews", String.valueOf(reviewNo), datePath, rename);
        String savedAt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());

        return new Stored(original, rename, ct, publicPath, savedAt);
    }

    /* 삭제 */
    public void deletePhysical(String publicPath) {
        // publicPath 예: static/reviews/{reviewNo}/yyyy/MM/uuid.jpg
        try {
            if (publicPath == null) return;
            String relative = publicPath.startsWith("static/") ? publicPath.substring("static/".length()) : publicPath;
            Path p = Paths.get(uploadRoot).resolve(relative).toAbsolutePath();
            Files.deleteIfExists(p);
        } catch (Exception ignore) { /* 로그만 남기고 계속 진행 */ }
    }

    @Getter @AllArgsConstructor
    public static class Stored {
        private final String originalName;
        private final String rename;
        private final String contentType;
        private final String publicPath;
        private final String savedAt; // VARCHAR(255) 호환
    }
}
