package com.eat.today.event.albti.command.application.service;

import com.eat.today.event.albti.command.application.dto.AlbtiMemberAddBulkRequestDTO;
import com.eat.today.event.albti.command.application.dto.AlbtiMemberAddBulkResponseDTO;
import com.eat.today.event.albti.command.application.entity.AlbtiAnswer;
import com.eat.today.event.albti.command.domain.repository.AlbtiAnswerRepository;
import com.eat.today.event.albti.command.domain.repository.AlbtiJoinMemberRepository;
import com.eat.today.event.albti.command.domain.repository.AlbtiMemberRepository;
import com.eat.today.event.albti.command.domain.repository.AlbtiSurveyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eat.today.member.command.application.service.MemberPointService;
import com.eat.today.member.command.domain.aggregate.PointPolicy;
import com.eat.today.event.albti.command.application.entity.AlbtiJoinMember;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlbtiMemberAddBulkService {

    private final AlbtiAnswerRepository albtiAnswerRepository;
    private final AlbtiMemberRepository albtiMemberRepository;
    private final AlbtiSurveyRepository albtiSurveyRepository;
    private final MemberPointService memberPointService;
    private final AlbtiJoinMemberRepository albtiJoinMemberRepository;

    @Transactional
    public AlbtiMemberAddBulkResponseDTO saveAllAnswers(AlbtiMemberAddBulkRequestDTO request) {

        // 회원 조회
        var member = albtiMemberRepository.findById(request.getMemberNo())
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

        // ✅ 재참여 시 기존 답변 삭제
        albtiAnswerRepository.deleteByMemberNo_MemberNo(request.getMemberNo());

        LocalDate today = LocalDate.now();
        boolean pointGranted = false; // ✅ 포인트 지급 여부 추적

        // ✅ 현재 회원 참여 이력 조회 (여기!! findByMemberNo 사용)
        var joinRecord = albtiJoinMemberRepository.findByMemberNo(request.getMemberNo());

        if (joinRecord.isPresent()) {
            // ✅ 이전 참여일과 오늘이 다르면 → 날짜 갱신 + 포인트 지급
            if (!joinRecord.get().getParticipatedAt().equals(today)) {
                joinRecord.get().setParticipatedAt(today);
                albtiJoinMemberRepository.save(joinRecord.get());
                memberPointService.grantPoints(request.getMemberNo(), PointPolicy.ALBTI_PARTICIPATE);
                pointGranted = true; // ✅ 지급됨
            }
        } else {
            // ✅ 첫 참여 → 참여 기록 생성 + 포인트 지급
            albtiJoinMemberRepository.save(
                    AlbtiJoinMember.builder()
                            .memberNo(request.getMemberNo())
                            .participatedAt(today)
                            .build()
            );
            memberPointService.grantPoints(request.getMemberNo(), PointPolicy.ALBTI_PARTICIPATE);
            pointGranted = true; // ✅ 지급됨
        }

        // ✅ 설문 답변 저장
        List<AlbtiAnswer> savedAnswers = request.getAnswers().stream().map(answerDto -> {

            var survey = albtiSurveyRepository.findById(answerDto.getAlbtiSurveyNo())
                    .orElseThrow(() -> new RuntimeException("설문 정보를 찾을 수 없습니다."));

            AlbtiAnswer answer = new AlbtiAnswer();
            answer.setMemberNo(member);
            answer.setAlbtiSurvey(survey);
            answer.setChoice(answerDto.getChoice());
            return albtiAnswerRepository.save(answer);

        }).collect(Collectors.toList());

        // 응답 DTO 변환
        List<AlbtiMemberAddBulkResponseDTO.AlbtiAnswerResponseDTO> responseList =
                savedAnswers.stream()
                        .map(saved -> new AlbtiMemberAddBulkResponseDTO.AlbtiAnswerResponseDTO(
                                saved.getAnswerNo(),
                                saved.getMemberNo().getMemberNo(),
                                saved.getAlbtiSurvey().getAlbtiSurveyNo(),
                                saved.getChoice()
                        )).collect(Collectors.toList());

        return new AlbtiMemberAddBulkResponseDTO(
                savedAnswers.size() + "건 설문 참여 기록 저장 완료",
                responseList,
                pointGranted // ✅ 프론트로 전달!
        );
    }
}