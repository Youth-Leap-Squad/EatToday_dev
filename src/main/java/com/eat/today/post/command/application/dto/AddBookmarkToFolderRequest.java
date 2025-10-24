package com.eat.today.post.command.application.dto;
import lombok.Getter;
@Getter
public class AddBookmarkToFolderRequest {
    private Integer folderId;
    private Integer boardNo;
}
