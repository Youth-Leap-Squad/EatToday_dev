package com.eat.today.sns.command.application.entity.prFileUpload;

import com.eat.today.sns.command.application.entity.photoReview.PhotoReviewEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "pr_file_upload")
@Data
public class PrFileUploadEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pr_file_no")
    private Integer prFileNo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "review_no")
    private PhotoReviewEntity review;

    @Column(name = "pr_file_name", nullable = false, length = 255)
    private String prFileName;

    @Column(name = "pr_file_type", nullable = false, length = 255)
    private String prFileType;

    @Column(name = "pr_file_rename", nullable = false, length = 255)
    private String prFileRename;

    @Column(name = "pr_file_path", nullable = false, length = 255)
    private String prFilePath;

    @Column(name = "pr_file_at", nullable = false, length = 255)
    private String prFileAt;
}