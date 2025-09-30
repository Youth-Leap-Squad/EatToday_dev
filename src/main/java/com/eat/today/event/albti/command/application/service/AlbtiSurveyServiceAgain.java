package com.eat.today.event.albti.command.application.service;

import com.eat.today.event.albti.command.application.dto.AlbtiSurveyAgainRequestDTO;
import com.eat.today.event.albti.command.application.dto.AlbtiSurveyAgainResponseDTO;
import com.eat.today.event.albti.command.application.entity.AlbtiJoinMember;
import com.eat.today.event.albti.command.application.entity.AlbtiSurvey;
import com.eat.today.event.albti.command.application.entity.AlbtiMember;
import com.eat.today.event.albti.command.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlbtiSurveyServiceAgain {

    private final AlbtiSurveyAgainMemberRepository albtiSurveyAgainMemberRepository;
    private final AlbtiSurveyAddRepository albtiSurveyAddRepository;
    private final AlbtiMemberRepository memberRepository;

    @Transactional
    public AlbtiSurveyAgainResponseDTO recheckSurvey(AlbtiSurveyAgainRequestDTO requestDTO) {

        // 1. 회원 존재 확인
        AlbtiMember member = memberRepository.findById(requestDTO.getMemberNo())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        // 2. 선택한 설문 존재 확인
        AlbtiSurvey newSurvey = albtiSurveyAddRepository.findById(requestDTO.getNewSurveyNo())
                .orElseThrow(() -> new IllegalArgumentException("선택한 술BTI 설문이 존재하지 않습니다."));

        // 3. 기존 참여 정보 조회
        AlbtiJoinMember joinMember = albtiSurveyAgainMemberRepository.findByMemberMemberNo(member.getMemberNo())
                .orElseThrow(() -> new IllegalArgumentException("참여 이력이 없습니다."));

        // 4. 기존 설문 → 새 설문으로 업데이트
        joinMember.setAlbtiSurvey(newSurvey);
        AlbtiJoinMember saved = albtiSurveyAgainMemberRepository.save(joinMember);

//        // 3. 기존 선택 삭제
//        albtiSurveyAgainMemberRepository.deleteByMember_MemberNo(member.getMemberNo());
//
//        // 4. 새 선택 저장
//        AlbtiJoinMember newJoin = new AlbtiJoinMember();
//        newJoin.setMember(member);
//        newJoin.setAlbtiSurvey(newSurvey);
//        AlbtiJoinMember saved = albtiSurveyAgainMemberRepository.save(newJoin);

        // 5. DTO 반환
        return new AlbtiSurveyAgainResponseDTO(
                saved.getAlbtiMemberNo(),
                member.getMemberNo(),
                newSurvey.getAlbtiSurveyNo(),
                newSurvey.getAlbti().getAlbtiNo(),
                newSurvey.getAlbti().getAlbtiCategory(),
                newSurvey.getAlbti().getAlbtiDetail()
        );
    }



//    /**
//     * 이전 검사 응답을 다시 선택 (재선택)
//     */
//    @Transactional
//    public AlbtiSurveyAgainResponseDTO reSelectSurvey(AlbtiSurveyAgainRequestDTO dto) {
//        // 1️⃣ 기존 응답 삭제
//        albtiSurveyAgainRepository.deleteByAlBTIMember_AlbtiMemberNo(dto.getAlBTIMemberNo());
//
//        // 2️⃣ 관련 엔티티 조회
//        AlbtiJoinMember joinMember = joinMemberRepository.findById(dto.getAlBTIMemberNo())
//                .orElseThrow(() -> new IllegalArgumentException("BTI 참여 회원 없음"));
//
//        Albti albti = albtiRepository.findById(dto.getAlbtiNo())
//                .orElseThrow(() -> new IllegalArgumentException("BTI 없음"));
//
//        // 3️⃣ 새 설문 응답 저장
//        AlbitSurveyAgain survey = new AlbitSurveyAgain();
//        survey.setAlbti(albti);
//        survey.setAlbtiSurveyContent(dto.getAlbtiSurveyContent());
//        survey.setAlBTIMember(joinMember);
//
//        AlbitSurveyAgain saved = albtiSurveyAgainRepository.save(survey);
//
//        // 4️⃣ 응답 DTO 반환
//        return new AlbtiSurveyAgainResponseDTO(
//                saved.getAlbtiSurveyNo(),
//                saved.getAlbti().getAlbtiNo(),
//                saved.getAlBTIMember().getAlbtiMemberNo(),
//                saved.getAlbtiSurveyContent()
//        );
//    }
}
