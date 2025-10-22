package com.eat.today.member.command.application.service;

import com.eat.today.member.command.domain.aggregate.MemberEntity;
import com.eat.today.member.command.domain.aggregate.PointPolicy;
import com.eat.today.member.command.infrastructure.persistence.SpringDataMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원 포인트 관리 서비스
 * 각 활동(로그인, 게시물 작성 등)에 대해 포인트를 지급하는 책임
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberPointService {

    private final SpringDataMemberRepository memberRepository;

    /**
     * 회원에게 포인트를 지급합니다.
     * @param memberNo 회원 번호
     * @param policy 포인트 정책
     */
    public void grantPoints(Integer memberNo, PointPolicy policy) {
        if (memberNo == null || policy == null) {
            log.warn("포인트 지급 실패: memberNo 또는 policy가 null입니다.");
            return;
        }

        memberRepository.findById(memberNo)
            .ifPresentOrElse(
                member -> {
                    member.addPoints(policy.getPoints());
                    memberRepository.save(member);
                    log.info("포인트 지급 완료 - 회원번호: {}, 활동: {}, 포인트: {}, 총포인트: {}", 
                            memberNo, policy.getDescription(), policy.getPoints(), member.getMemberLevel());
                },
                () -> log.warn("포인트 지급 실패: 회원번호 {}를 찾을 수 없습니다.", memberNo)
            );
    }

    /**
     * 회원의 현재 포인트를 조회합니다.
     * @param memberNo 회원 번호
     * @return 회원의 총 포인트
     */
    public Integer getMemberPoints(Integer memberNo) {
        return memberRepository.findById(memberNo)
            .map(MemberEntity::getMemberLevel)
            .orElse(0);
    }
}

