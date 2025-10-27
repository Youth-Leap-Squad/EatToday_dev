package com.eat.today.post.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="bookmark_folder",
        uniqueConstraints=@UniqueConstraint(name="ux_member_foldername",columnNames={"member_no","folder_name"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BookmarkFolder {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="folder_id") private Integer folderId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_no", nullable=false)
    private Member member;

    @Column(name="folder_name", nullable=false, length=100)
    private String folderName;
}
