package com.eat.today.qna_rounge_report.report.command.application.service;

import com.eat.today.member.command.domain.repository.ReportMemberRepository;
import com.eat.today.qna_rounge_report.report.command.domain.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// ReportProcessService
@Service
@RequiredArgsConstructor
@Transactional
public class ReportProcessService {

    private final ReportRepository reportRepository;
    private final ReportMemberRepository memberRepository;

    public void confirm(Integer reportId) {
        // 1) 신고 N → Y 시도 (이미 Y면 증가 안 함)
        int changed = reportRepository.markProcessedIfNot(reportId);
        if (changed == 0) return;

        // 2) 피신고자 member_no 조회
        Integer reportedMemberNo = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("신고 없음: " + reportId))
                .getReportedId();

        // 3) 회원 신고 카운트 + 상태를 DB에서 직접 갱신
        memberRepository.bumpCountAndStatus(reportedMemberNo);
    }
}