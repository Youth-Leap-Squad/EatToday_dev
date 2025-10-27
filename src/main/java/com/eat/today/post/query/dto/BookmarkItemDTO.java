package com.eat.today.post.query.dto;
import lombok.*;
import java.time.LocalDateTime;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BookmarkItemDTO {
    private int boardNo;
    private String title;
    private String boardDate;
}
