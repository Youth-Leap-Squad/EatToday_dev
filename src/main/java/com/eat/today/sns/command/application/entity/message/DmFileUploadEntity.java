package com.eat.today.sns.command.application.entity.message;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "dm_file_upload")
@Data
public class DmFileUploadEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dm_file_no")
    private Integer dmFileNo;

    @Column(name = "note_id", nullable = false)
    private Integer noteId;

    @Column(name = "dm_file_name", nullable = false, length = 255)
    private String dmFileName;

    @Column(name = "dm_file_type", nullable = false, length = 255)
    private String dmFileType;

    @Column(name = "dm_file_rename", nullable = false, length = 255)
    private String dmFileRename;

    @Column(name = "dm_file_path", nullable = false, length = 255)
    private String dmFilePath;

    @Column(name = "dm_file_at", nullable = false, length = 255)
    private String dmFileAt;

    @Column(name = "dm_file_size", nullable = false)
    private Long dmFileSize;
}
