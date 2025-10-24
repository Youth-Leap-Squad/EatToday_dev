package com.eat.today.post.query.dto;
import lombok.*;
import java.time.LocalDateTime;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BookmarkFolderDTO {
    private int folderId;
    private String folderName;
    private int itemCount;
    private LocalDateTime createdAt;
}
