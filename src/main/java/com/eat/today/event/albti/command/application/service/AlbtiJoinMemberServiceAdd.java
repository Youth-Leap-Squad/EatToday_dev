package com.eat.today.event.albti.command.application.service;

import com.eat.today.event.albti.command.application.dto.AlbtiJoinMemberAddRequestDTO;
import com.eat.today.event.albti.command.application.dto.AlbtiJoinMemberAddResponseDTO;
import com.eat.today.event.albti.command.application.entity.AlbtiJoinMember;
import com.eat.today.event.albti.command.application.entity.AlbtiSurvey;
import com.eat.today.event.albti.command.application.entity.AlbtiMember;
import com.eat.today.event.albti.command.domain.repository.AlbtiJoinMemberRepository;
import com.eat.today.event.albti.command.domain.repository.AlbtiSurveyAddRepository;
import com.eat.today.event.albti.command.domain.repository.AlbtiMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlbtiJoinMemberServiceAdd {

    private final AlbtiJoinMemberRepository joinMemberRepository;
    private final AlbtiSurveyAddRepository  surveyAddRepository;
    private final AlbtiMemberRepository memberRepository;

    public AlbtiJoinMemberAddResponseDTO addParticipant(AlbtiJoinMemberAddRequestDTO requestDTO) {
        // 1. 회원 존재 여부 확인
        AlbtiMember member = memberRepository.findById(requestDTO.getMemberNo())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        // 2. 선택한 BTI 결과 존재 확인
        AlbtiSurvey albtiSurvey = surveyAddRepository.findById(requestDTO.getAlbtiSurveyNo())
                .orElseThrow(() -> new IllegalArgumentException("해당 술BTI 결과가 존재하지 않습니다."));

        // 3. 참여자가 어떤 설문번호를 눌렀는지 엔티티 생성
        AlbtiJoinMember participant = new AlbtiJoinMember();
        participant.setMember(member);
        participant.setAlbtiSurvey(albtiSurvey);

        // 4. DB 저장
        AlbtiJoinMember saved = joinMemberRepository.save(participant);

        // 5. 응답 DTO 반환
        return new AlbtiJoinMemberAddResponseDTO(
                saved.getAlbtiMemberNo(),
                saved.getMember().getMemberNo(),
                saved.getAlbtiSurvey().getAlbtiSurveyNo()
        );
    }
}

//@Transactional
//public void joinWorldcup(int memberNo, int worldcupNo, int alcoholId, int foodId) {
//    // 1. 회원이 월드컵에 참여 (worldcup_join_member insert)
//    WorldcupJoinMember joinMember = new WorldcupJoinMember();
//    joinMember.setMemberNo(memberNo);
//    joinMember.setWorldcupNo(worldcupNo);
//    WorldcupJoinMember savedJoinMember = joinMemberRepository.save(joinMember);     // DB insert
//
//    // 2. 회원 pick 저장 (world_cup_picks insert)
//    WorldcupPicks picks = new WorldcupPicks();
//    picks.setWorldcupJoinMemberNo(savedJoinMember.getWorldcupJoinMemberNo());       // savedJoinMember에서 FK 연결
//    picks.setWorldcupAlcoholNo(alcoholId);
//    picks.setIndividualFood(foodId);
//    picksRepository.save(picks);
//}
