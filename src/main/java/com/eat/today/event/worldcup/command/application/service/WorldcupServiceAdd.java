package com.eat.today.event.worldcup.command.application.service;


import com.eat.today.event.worldcup.command.application.entity.WorldcupJoinMember;
import com.eat.today.event.worldcup.command.application.entity.WorldcupPicks;
import com.eat.today.event.worldcup.command.domain.repository.WorldcupJoinMemberRepository;
import com.eat.today.event.worldcup.command.domain.repository.WorldcupPicksRepository;
import com.eat.today.member.command.application.service.MemberPointService;
import com.eat.today.member.command.domain.aggregate.PointPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorldcupServiceAdd {

    private final WorldcupJoinMemberRepository joinMemberRepository;
    private final WorldcupPicksRepository picksRepository;
    private final MemberPointService memberPointService;


    @Transactional
    public int joinWorldcup(int memberNo, int worldcupNo, int alcoholId, int foodId) {
        // 1. 회원이 월드컵에 참여 (worldcup_join_member insert)
        WorldcupJoinMember joinMember = new WorldcupJoinMember();
        joinMember.setMemberNo(memberNo);
        joinMember.setWorldcupNo(worldcupNo);
        WorldcupJoinMember savedJoinMember = joinMemberRepository.save(joinMember);     // DB insert

        // 2. 회원 pick 저장 (world_cup_picks insert)
        WorldcupPicks picks = new WorldcupPicks();
        picks.setWorldcupJoinMemberNo(savedJoinMember.getWorldcupJoinMemberNo());       // savedJoinMember에서 FK 연결
        picks.setWorldcupAlcoholNo(alcoholId);
        picks.setIndividualFood(foodId);
        picksRepository.save(picks);
        
        // 3. 월드컵 게임 참여 시 포인트 지급
        try {
            memberPointService.grantPoints(memberNo, PointPolicy.WORLDCUP_PARTICIPATE);
        } catch (Exception e) {
            log.error("월드컵 게임 참여 포인트 지급 실패 - 회원번호: {}", memberNo, e);
        }

        // PK 반환
        return savedJoinMember.getWorldcupJoinMemberNo();

    }
}
