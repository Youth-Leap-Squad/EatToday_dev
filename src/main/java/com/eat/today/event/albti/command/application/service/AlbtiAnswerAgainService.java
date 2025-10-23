package com.eat.today.event.albti.command.application.service;


import com.eat.today.event.albti.command.application.dto.AlbtiAnswerAgainRequestDTO;
import com.eat.today.event.albti.command.application.dto.AlbtiAnswerAgainResponceDTO;
import com.eat.today.event.albti.command.application.entity.AlbtiAnswer;
//import com.eat.today.event.albti.command.application.entity.AlbtiJoinMember;
import com.eat.today.event.albti.command.application.entity.AlbtiMember;
import com.eat.today.event.albti.command.application.entity.AlbtiSurvey;
import com.eat.today.event.albti.command.domain.repository.AlbtiAnswerRepository;
//import com.eat.today.event.albti.command.domain.repository.AlbtiJoinMemberRepository;
import com.eat.today.event.albti.command.domain.repository.AlbtiMemberRepository;
import com.eat.today.event.albti.command.domain.repository.AlbtiSurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlbtiAnswerAgainService {
    private final AlbtiMemberRepository albtiMemberRepository;
    private final AlbtiSurveyRepository albtiSurveyRepository;
    private final AlbtiAnswerRepository albtiAnswerRepository;
//    private final AlbtiJoinMemberRepository albtiJoinMemberRepository;

    @Transactional
    public AlbtiAnswerAgainResponceDTO recheckAnswer(AlbtiAnswerAgainRequestDTO requestDTO) {

        // 1. 회원 존재 확인
        AlbtiMember member = albtiMemberRepository.findById(requestDTO.getMemberNo())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        // 2. 설문 존재 확인
        AlbtiSurvey survey = albtiSurveyRepository.findById(requestDTO.getAlbtiSurveyNo())
                .orElseThrow(() -> new IllegalArgumentException("해당 술BTI 설문이 존재하지 않습니다."));

//        // 3. 선택한 응답 존재 확인
//        AlbtiAnswer newAnswer = albtiAnswerRepository.findById(requestDTO.getAlbtiSurveyNo())
//                .orElseThrow(() -> new IllegalArgumentException("선택한 술BTI 응답이 존재하지 않습니다."));
//
//        // 4. 기존 참여 정보 조회
//        AlbtiJoinMember joinMember = albtiJoinMemberRepository.findByMemberMemberNo(member.getMemberNo())
//                .orElseThrow(() -> new IllegalArgumentException("참여 이력이 없습니다."));

        // 3 ) 기존 응답 조회 (반드시 존재해야 함)
        AlbtiAnswer existingAnswer = albtiAnswerRepository
                .findByMemberNo_MemberNoAndAlbtiSurvey_AlbtiSurveyNo(member.getMemberNo(), survey.getAlbtiSurveyNo())
                .orElseThrow(() -> new IllegalArgumentException("기존 응답이 존재하지 않습니다. 재선택은 기존 응답이 있는 경우에만 가능합니다."));

        // 4 ) 기존 응답 choice 수정
        existingAnswer.setChoice(requestDTO.getChoice());

        // 5 ) 저장
        AlbtiAnswer savedAnswer = albtiAnswerRepository.save(existingAnswer);

        // 6 ) 응답 DTO 반환
        return new AlbtiAnswerAgainResponceDTO(
                savedAnswer.getAnswerNo(),
                member.getMemberNo(),
                survey.getAlbtiSurveyNo(),
                savedAnswer.getChoice(),
                "응답이 성공적으로 수정되었습니다."
        );

//        // 5. 기존 응답 → 새 응답으로 업데이트
//        joinMember.setAlbtiAnswer(newAnswer);
//        AlbtiJoinMember saved = albtiAnswerRepository.save(joinMember);

//        // 6. DTO 반환
//        return new AlbtiSurveyAgainResponseDTO(
//                saved.getAlbtiMemberNo(),
//                member.getMemberNo(),
//                newSurvey.getAlbtiSurveyNo(),
//                newSurvey.getAlbti().getAlbtiNo(),
//                newSurvey.getAlbti().getAlbtiCategory(),
//                newSurvey.getAlbti().getAlbtiDetail()
//        );

    }
}
