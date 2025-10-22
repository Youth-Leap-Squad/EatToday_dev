package com.eat.today.post.command.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageStorageService {

    @Value("${app.upload.dir:/var/eattoday/uploads}")
    private String uploadDir;

    @Value("${app.upload.public-base-url:http://localhost:8003/uploads}")
    private String publicBaseUrl;

    public String store(MultipartFile file, String subDir) {
        if (file == null || file.isEmpty()) return null;

        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + (ext != null ? "." + ext.toLowerCase() : "");
        Path dir = Paths.get(uploadDir, subDir == null ? "" : subDir).normalize().toAbsolutePath();

        try {
            Files.createDirectories(dir);
            Path target = dir.resolve(filename).normalize();

            String ct = file.getContentType();
            if (ct == null || !ct.startsWith("image/")) {
                throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
            }
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }

        String subPath = (subDir == null || subDir.isBlank()) ? filename : (subDir + "/" + filename);
        return publicBaseUrl.replaceAll("/$", "") + "/" + subPath;
    }

    public List<String> storeAll(MultipartFile[] files, String subDir) {
        if (files == null || files.length == 0) return List.of();
        List<String> urls = new ArrayList<>();
        for (MultipartFile f : files) {
            if (f != null && !f.isEmpty()) {
                String url = store(f, subDir);
                if (url != null) urls.add(url);
            }
        }
        return urls;
    }
}
