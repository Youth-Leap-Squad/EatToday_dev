package com.eat.today.qna_rounge_report.report.command.application.service;


import com.eat.today.member.command.domain.aggregate.MemberEntity;
import com.eat.today.member.command.domain.repository.MemberRepository;
import com.eat.today.qna_rounge_report.report.command.domain.aggregate.Report;
import com.eat.today.qna_rounge_report.report.command.domain.repository.ReportRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ReportProcessService_UserTest {
    @Autowired ReportProcessService reportProcessService;
    @Autowired MemberRepository memberRepository;
    @Autowired ReportRepository reportRepository;

    @Test
    void 신고처리시_member상태가_업데이트된다() {
        // given: 회원 저장
        MemberEntity member = new MemberEntity();
        member.setMemberPhone("010-1111-2222");
        member.setMemberStatus("normal");
        member.setMemberActive(true);
        member = memberRepository.save(member);

        // 그리고 이 회원을 대상으로 한 신고 저장
        Report report = new Report();
        report.
        report.setreportedIdmember();
        report.setContent("욕설");
        report = reportRepository.save(report);

        // when: 신고 처리 실행
        reportProcessService.process(report.getReportId());

        // then: DB에서 다시 조회하여 값이 실제로 바뀌었는지 확인
        MemberEntity reloaded = memberRepository.findById(member.getMemberNo()).orElseThrow();
        assertThat(reloaded.getMemberStatus()).isEqualTo("suspended"); // 예: 처리 규칙에 맞춰 기대값
        assertThat(reloaded.getMemberActive()).isFalse();
    }
}
