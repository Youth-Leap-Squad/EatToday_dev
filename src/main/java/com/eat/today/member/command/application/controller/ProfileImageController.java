package com.eat.today.member.command.application.controller;

import com.eat.today.member.command.application.dto.ProfileImageResponseDTO;
import com.eat.today.member.command.application.service.ProfileImageService;
import com.eat.today.member.command.domain.aggregate.ProfileImageEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class ProfileImageController {

    private final ProfileImageService profileImageService;
    private final ModelMapper modelMapper;

    @Value("${app.upload.path:/uploads/profile}")
    private String uploadPath;

    /**
     * 프로필 사진 업로드
     */
    @PostMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadProfileImage(
            @RequestParam("memberNo") Integer memberNo,
            @RequestParam("file") MultipartFile file) {
        
        try {
            // 파일 저장
            String storedFileName = profileImageService.saveProfileImage(file);
            String imageUrl = "http://localhost:8080/uploads/profile/" + storedFileName;
            
            // member 테이블에 프로필 이미지 URL 업데이트
            profileImageService.updateMemberProfileImage(memberNo, imageUrl);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "프로필 사진이 성공적으로 업로드되었습니다.");
            response.put("status", "success");
            response.put("imageUrl", imageUrl);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("프로필 사진 업로드 실패: {}", memberNo, e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "프로필 사진 업로드에 실패했습니다: " + e.getMessage());
            response.put("status", "error");
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    // 프로필 사진 조회 (member_no 기준)
    @GetMapping("/profile-image/{memberNo}")
    public ResponseEntity<Resource> getProfileImage(@PathVariable Integer memberNo) throws IOException {
        // DB에 저장된 건 "전체 URL" 말고 "파일명"을 저장하는 걸 권장
        // 지금은 imageUrl이 전체 URL일 수도 있으니 둘 다 처리
        String imageUrl = profileImageService.getProfileImageUrl(memberNo);

        if (imageUrl == null || imageUrl.isBlank()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 1) 절대 URL(S3/정적서버)인 경우 → 302로 바로 리다이렉트
        if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, imageUrl)
                    .build();
        }

        // 2) 파일명인 경우 → 로컬에서 읽어 스트리밍
        Path file = Paths.get(uploadPath).resolve(imageUrl).normalize();
        Resource resource = new UrlResource(file.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String mime = Optional.ofNullable(java.nio.file.Files.probeContentType(file))
                .orElse("image/jpeg"); // 기본값
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mime))
                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                .header(HttpHeaders.PRAGMA, "no-cache")
                .header(HttpHeaders.EXPIRES, "0")
                .body(resource);
    }

    /**
     * 프로필 사진 파일 다운로드/조회 (이미지 ID로)
     */
    @GetMapping("/profile-image/file/{imageId}")
    public ResponseEntity<Resource> getProfileImageFile(@PathVariable Long imageId) {
        try {
            // 이미지 ID로 파일 정보 조회 (간단한 구현)
            // 실제로는 ProfileImageRepository에서 ID로 조회해야 함
            Path filePath = Paths.get(uploadPath).resolve("default-profile.png");
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"profile.jpg\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (MalformedURLException e) {
            log.error("프로필 사진 파일 조회 실패: {}", imageId, e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 프로필 사진 삭제
     */
    @DeleteMapping("/profile-image/{memberEmail}")
    public ResponseEntity<Map<String, String>> deleteProfileImage(@PathVariable String memberEmail) {
        try {
            profileImageService.deleteProfileImage(memberEmail);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "프로필 사진이 삭제되었습니다.");
            response.put("status", "success");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("프로필 사진 삭제 실패: {}", memberEmail, e);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "프로필 사진 삭제에 실패했습니다.");
            response.put("status", "error");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 기본 프로필 사진 설정
     */
    @PostMapping("/profile-image/{memberEmail}/set-default")
    public ResponseEntity<Map<String, String>> setDefaultProfileImage(@PathVariable String memberEmail) {
        try {
            profileImageService.setDefaultProfileImage(memberEmail);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "기본 프로필 사진으로 설정되었습니다.");
            response.put("status", "success");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("기본 프로필 사진 설정 실패: {}", memberEmail, e);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "기본 프로필 사진 설정에 실패했습니다.");
            response.put("status", "error");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

