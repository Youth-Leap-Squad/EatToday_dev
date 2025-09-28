package com.eat.today.qna_rounge_report.qna.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "qna_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class QnaComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_no")
    private Integer id;

    @Column(name = "qna_post_no", nullable = false)
    private Integer qnaPostNo;

    @Column(name = "comment_member_no", nullable = false)
    private Integer commentMemberNo;

    @Column(name = "comment_content", nullable = false, length = 255)
    private String commentContent;

    @Column(name = "comment_at", nullable = false, length = 255)
    private String commentAt;

    public QnaComment(Integer qnaPostNo, Integer commentMemberNo,
                      String commentContent, String commentAt) {
        this.qnaPostNo = qnaPostNo;
        this.commentMemberNo = commentMemberNo;
        this.commentContent = commentContent;
        this.commentAt = commentAt;
    }

    public void updateComment(String newContent) {
        this.commentContent = newContent;
    }
}