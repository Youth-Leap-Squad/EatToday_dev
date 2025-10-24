package com.eat.today.post.command.application.dto;


import lombok.Data;

@Data
public class RenameFolderRequest {
    private Long folderId;
    private String folderName;
}
