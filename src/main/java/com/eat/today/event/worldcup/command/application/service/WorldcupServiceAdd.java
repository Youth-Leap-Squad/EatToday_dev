package com.eat.today.event.worldcup.command.application.service;


import com.eat.today.event.worldcup.command.application.entity.WorldcupJoinMember;
import com.eat.today.event.worldcup.command.application.entity.WorldcupPicks;
//import com.eat.today.event.worldcup.command.domain.repository.WorldcupJoinMemberAlcoholRepository;
import com.eat.today.event.worldcup.command.domain.repository.WorldcupJoinMemberRepository;
import com.eat.today.event.worldcup.command.domain.repository.WorldcupPicksRepository;
import com.eat.today.member.command.application.service.MemberPointService;
import com.eat.today.member.command.domain.aggregate.PointPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorldcupServiceAdd {

    private final WorldcupJoinMemberRepository joinMemberRepository;
//    private final WorldcupJoinMemberAlcoholRepository worldcupJoinMemberAlcoholRepository;
    private final WorldcupPicksRepository picksRepository;
    private final MemberPointService memberPointService;

    @Transactional
    public int joinWorldcup(int memberNo, int worldcupNo, int alcoholId, int foodId) {

        // ✅ 참여 저장
        WorldcupJoinMember joinMember = new WorldcupJoinMember();
        joinMember.setMemberNo(memberNo);
        joinMember.setWorldcupNo(worldcupNo);
        joinMember.setAlcoholId(alcoholId);
        joinMember.setParticipatedAt(LocalDate.now()); // ✅ 추가
        WorldcupJoinMember savedJoinMember = joinMemberRepository.save(joinMember);

        // ✅ PICK 저장
        WorldcupPicks picks = new WorldcupPicks();
        picks.setWorldcupJoinMemberNo(savedJoinMember.getWorldcupJoinMemberNo());
        picks.setWorldcupAlcoholNo(alcoholId);
        picks.setIndividualFood(foodId);
        picksRepository.save(picks);

        // ✅ 포인트 지급 (매 참여 시 무조건)
        memberPointService.grantPoints(memberNo, PointPolicy.WORLDCUP_PARTICIPATE);
        log.info("✅ 월드컵 포인트 지급 - memberNo={}, +30P", memberNo);

        return savedJoinMember.getWorldcupJoinMemberNo();
    }
}