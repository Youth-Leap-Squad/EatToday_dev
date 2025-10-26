package com.eat.today.post.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="bookmark")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bookmark {
    @EmbeddedId private BookmarkKey id;

    @MapsId("folderId")
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="folder_id", nullable=false)
    private BookmarkFolder folder;

    @MapsId("boardNo")
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="board_no", nullable=false)
    private FoodPost post;
}