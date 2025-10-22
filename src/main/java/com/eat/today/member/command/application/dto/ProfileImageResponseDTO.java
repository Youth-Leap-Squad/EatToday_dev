package com.eat.today.member.command.application.dto;

import lombok.Data;

@Data
public class ProfileImageResponseDTO {
    private Long id;
    private String memberEmail;
    private String originalFileName;
    private String storedFileName;
    private String filePath;
    private Long fileSize;
    private String contentType;
    private String uploadedAt;
    private boolean isActive;
    private boolean isDefault;
    private String imageUrl; // 웹에서 접근 가능한 URL
}

