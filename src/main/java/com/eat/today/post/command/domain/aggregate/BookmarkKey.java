package com.eat.today.post.command.domain.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class BookmarkKey implements Serializable {
    @Column(name="folder_id") private Integer folderId;
    @Column(name="board_no")  private Integer boardNo;
}

