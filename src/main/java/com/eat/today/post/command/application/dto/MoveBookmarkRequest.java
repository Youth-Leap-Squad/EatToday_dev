package com.eat.today.post.command.application.dto;
import lombok.Getter;
@Getter
public class MoveBookmarkRequest {
    private Integer fromFolderId;
    private Integer toFolderId;
    private Integer boardNo;
}
