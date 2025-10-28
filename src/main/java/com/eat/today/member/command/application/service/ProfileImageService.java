package com.eat.today.member.command.application.service;

import com.eat.today.member.command.domain.aggregate.MemberEntity;
import com.eat.today.member.command.domain.aggregate.ProfileImageEntity;
import com.eat.today.member.command.domain.repository.MemberRepository;
import com.eat.today.member.command.domain.repository.ProfileImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProfileImageService {

    private final ProfileImageRepository profileImageRepository;
    private final MemberRepository memberRepository;

    @Value("${app.upload.path:/uploads/profile}")
    private String uploadPath;

    @Value("${app.upload.max-size:5242880}") // 5MB
    private long maxFileSize;

    /**
     * 프로필 사진 업로드
     */
    public ProfileImageEntity uploadProfileImage(String memberEmail, MultipartFile file) {
        try {
            // 파일 유효성 검사
            validateFile(file);

            // 기존 활성화된 프로필 사진 비활성화
            deactivateExistingProfileImages(memberEmail);

            // 파일 저장
            String storedFileName = generateStoredFileName(file.getOriginalFilename());
            String filePath = saveFile(file, storedFileName);

            // 데이터베이스에 저장
            ProfileImageEntity profileImage = new ProfileImageEntity();
            profileImage.setMemberEmail(memberEmail);
            profileImage.setOriginalFileName(file.getOriginalFilename());
            profileImage.setStoredFileName(storedFileName);
            profileImage.setFilePath(filePath);
            profileImage.setFileSize(file.getSize());
            profileImage.setContentType(file.getContentType());
            profileImage.setUploadedAt(LocalDateTime.now());
            profileImage.setActive(true);
            profileImage.setDefault(false);

            ProfileImageEntity savedImage = profileImageRepository.save(profileImage);
            log.info("프로필 사진 업로드 완료: {} -> {}", memberEmail, storedFileName);

            return savedImage;

        } catch (Exception e) {
            log.error("프로필 사진 업로드 실패: {}", memberEmail, e);
            throw new RuntimeException("프로필 사진 업로드에 실패했습니다.", e);
        }
    }

    /**
     * 파일 유효성 검사
     */
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }

        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("파일 크기가 너무 큽니다. (최대 5MB)");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
        }

        // 허용된 이미지 타입 확인
        String[] allowedTypes = {"image/jpeg", "image/jpg", "image/png", "image/gif"};
        boolean isAllowed = false;
        for (String allowedType : allowedTypes) {
            if (contentType.equals(allowedType)) {
                isAllowed = true;
                break;
            }
        }

        if (!isAllowed) {
            throw new IllegalArgumentException("지원하지 않는 이미지 형식입니다. (JPEG, PNG, GIF만 지원)");
        }
    }

    /**
     * 기존 활성화된 프로필 사진 비활성화
     */
    private void deactivateExistingProfileImages(String memberEmail) {
        profileImageRepository.findByMemberEmailOrderByUploadedAtDesc(memberEmail)
                .forEach(image -> {
                    image.setActive(false);
                    profileImageRepository.save(image);
                });
    }

    /**
     * 저장용 파일명 생성
     */
    private String generateStoredFileName(String originalFileName) {
        String extension = getFileExtension(originalFileName);
        return UUID.randomUUID().toString() + extension;
    }

    /**
     * 파일 확장자 추출
     */
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return fileName.substring(lastDotIndex);
    }

    /**
     * 파일을 디스크에 저장
     */
    private String saveFile(MultipartFile file, String storedFileName) throws IOException {
        // 업로드 디렉토리 생성
        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        // 파일 저장
        Path filePath = uploadDir.resolve(storedFileName);
        Files.copy(file.getInputStream(), filePath);

        return filePath.toString();
    }

    /**
     * 회원의 활성화된 프로필 사진 조회
     */
    @Transactional(readOnly = true)
    public Optional<ProfileImageEntity> getActiveProfileImage(String memberEmail) {
        return profileImageRepository.findByMemberEmailAndIsActiveTrue(memberEmail);
    }

    /**
     * 프로필 사진 삭제
     */
    public void deleteProfileImage(String memberEmail) {
        try {
            // 데이터베이스에서 삭제
            profileImageRepository.deleteByMemberEmail(memberEmail);
            log.info("프로필 사진 삭제 완료: {}", memberEmail);
        } catch (Exception e) {
            log.error("프로필 사진 삭제 실패: {}", memberEmail, e);
            throw new RuntimeException("프로필 사진 삭제에 실패했습니다.", e);
        }
    }

    /**
     * 기본 프로필 사진 설정
     */
    public void setDefaultProfileImage(String memberEmail) {
        Optional<ProfileImageEntity> activeImage = getActiveProfileImage(memberEmail);
        if (activeImage.isPresent()) {
            ProfileImageEntity image = activeImage.get();
            image.setDefault(true);
            profileImageRepository.save(image);
            log.info("기본 프로필 사진 설정 완료: {}", memberEmail);
        }
    }
    
    // === 새로운 간단한 방법 ===
    
    /**
     * 파일만 저장하고 파일명 반환
     */
    public String saveProfileImage(MultipartFile file) throws IOException {
        validateFile(file);
        String storedFileName = generateStoredFileName(file.getOriginalFilename());
        saveFile(file, storedFileName);
        return storedFileName;
    }
    
    /**
     * member 테이블에 프로필 이미지 URL 업데이트
     */
    public void updateMemberProfileImage(Integer memberNo, String imageUrl) {
        Optional<MemberEntity> memberOpt = memberRepository.findById(memberNo);
        if (memberOpt.isPresent()) {
            MemberEntity member = memberOpt.get();
            member.setProfileImageUrl(imageUrl);
            memberRepository.save(member);
            log.info("회원 {} 프로필 이미지 업데이트: {}", memberNo, imageUrl);
        } else {
            throw new RuntimeException("회원을 찾을 수 없습니다: " + memberNo);
        }
    }
    
    /**
     * member_no로 프로필 이미지 URL 조회
     */
    @Transactional(readOnly = true)
    public String getProfileImageUrl(Integer memberNo) {
        Optional<MemberEntity> memberOpt = memberRepository.findById(memberNo);
        return memberOpt.map(MemberEntity::getProfileImageUrl).orElse(null);
    }
}
