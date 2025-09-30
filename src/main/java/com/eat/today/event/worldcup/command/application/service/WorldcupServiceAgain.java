package com.eat.today.event.worldcup.command.application.service;

import com.eat.today.event.worldcup.command.application.dto.WorldcupJoinRequestAddAgainDTO;
import com.eat.today.event.worldcup.command.application.dto.WorldcupJoinResponseAgainDTO;
import com.eat.today.event.worldcup.command.application.entity.WorldcupJoinMember;
import com.eat.today.event.worldcup.command.application.entity.WorldcupPicks;
import com.eat.today.event.worldcup.command.domain.repository.WorldcupJoinMemberRepository;
import com.eat.today.event.worldcup.command.domain.repository.WorldcupPicksAgainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorldcupServiceAgain {

    private final WorldcupPicksAgainRepository worldcupPicksAgainRepository;
    private final WorldcupJoinMemberRepository  worldcupJoinMemberRepository;

    @Transactional
    public WorldcupJoinResponseAgainDTO rePick(WorldcupJoinRequestAddAgainDTO dto) {

        // 0. joinMemberRepository 필요 → 생성자 주입 추가
        WorldcupJoinMember joinMember = worldcupJoinMemberRepository
                .findByMemberNoAndWorldcupNo(dto.getMemberNo(), dto.getWorldcupNo())
                .orElseThrow(() -> new RuntimeException("참여 정보 없음"));

        int joinMemberNo = joinMember.getWorldcupJoinMemberNo();


        // 1. 기존 선택 삭제
        // dto.getMemberNo()와 같은 회원 번호(worldcup_join_member_no)를 가진 world_cup_picks 테이블의 데이터가 전부 삭제됩니다.
        worldcupPicksAgainRepository.deleteByworldcupJoinMemberNo(joinMemberNo);

        // 2. 새로운 선택 삽입
        WorldcupPicks pick = WorldcupPicks.builder()
                .worldcupJoinMemberNo(joinMemberNo)
                .worldcupAlcoholNo(dto.getAlcoholId())
                .individualFood(dto.getFoodId())
                .build();

        worldcupPicksAgainRepository.save(pick);

        // 3. ResponseDTO 반환
        return new WorldcupJoinResponseAgainDTO(
                dto.getMemberNo(),
                dto.getAlcoholId(),
                dto.getFoodId(),
                "월드컵 선택이 다시 저장되었습니다!");
    }
}
