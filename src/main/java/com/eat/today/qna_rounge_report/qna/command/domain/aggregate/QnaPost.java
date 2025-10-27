package com.eat.today.qna_rounge_report.qna.command.domain.aggregate;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "qna_post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class QnaPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_post_no")
    private Integer id;

    @Column(name = "member_no", nullable = false)
    private Integer memberNo;

    @Column(name = "inquiry_title", nullable = false, length = 255)
    private String inquiryTitle;

    @Column(name = "Inquiry_content", nullable = false, length = 255)
    private String inquiryContent;

    @Column(name = "Inquiry_at", nullable = false, length = 255)
    private String inquiryAt;

    public QnaPost(Integer memberNo, String inquiryTitle, String inquiryContent, String inquiryAt) {
        this.memberNo = memberNo;
        this.inquiryTitle = inquiryTitle;
        this.inquiryContent = inquiryContent;
        this.inquiryAt = inquiryAt;
    }

    // 문의 내용 수정
    public void updateContent(String newContent) {
        this.inquiryContent = newContent;
    }
}
