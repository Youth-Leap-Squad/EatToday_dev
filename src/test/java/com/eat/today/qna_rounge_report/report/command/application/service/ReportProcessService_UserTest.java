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

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ReportProcessService_UserTest {

    @Autowired ReportProcessService reportProcessService;
    @Autowired MemberRepository memberRepository;
    @Autowired ReportRepository reportRepository;
    @PersistenceContext EntityManager em;

    @Test
    void 신고처리시_member상태와_reportCount가_업데이트된다() {
        // given: 회원 저장
        MemberEntity member = new MemberEntity();
        member.setMemberPhone("010-1111-2222");
        member.setMemberPw("test123");
        member.setMemberName("홍길동");
        member.setMemberId("hong");
        member.setMemberBirth("1999-01-01");
        member = memberRepository.save(member);

        Report report = new Report(
                999,
                member.getMemberNo(),
                "비매너",
                "욕설",
                "2025-09-29",
                "게시글"
        );
        report = reportRepository.save(report);

        // when: 신고 확정 실행
        reportProcessService.confirm(report.getId());

        em.flush();
        em.clear();

        // then: DB에서 다시 조회하여 값 검증
        MemberEntity reloaded = memberRepository.findById(member.getMemberNo())
                .orElseThrow();

        assertThat(reloaded.getReportCount()).isEqualTo(1);
        assertThat(reloaded.getMemberStatus()).isNotEqualTo("normal");
    }
}