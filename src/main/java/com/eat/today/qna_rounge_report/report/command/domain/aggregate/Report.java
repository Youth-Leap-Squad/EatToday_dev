package com.eat.today.qna_rounge_report.report.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name ="report")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_no")
    private Integer id;

    /** 신고한 회원 번호 */
    @Column(name = "member_no2", nullable = false)
    private Integer reporterId;


    /** 신고 당한 회원 번호 */
    @Column(name = "member_no", nullable = false)
    private Integer reportedId;

    @Column(name = "report_title", nullable = false, length = 255)
    private String title;

    @Column(name = "report_content", nullable = false, length = 255)
    private String content;

    @Column(name = "report_yn", nullable = false)
    private Boolean processed = false;

    @Column(name = "report_date", nullable = false, length = 255)
    private String reportDate;

    @Column(name = "report_source", nullable = false, length = 255)
    private String source;

    public Report(Integer reporterId, Integer reportedId, String title, String content, String reportDate, String source) {
        this.reporterId = reporterId;
        this.reportedId = reportedId;
        this.title = title;
        this.content = content;
        this.reportDate = reportDate;
        this.source = source;
        this.processed = false;
    }

    public void markProcessed() {
        this.processed = true;
    }

}
