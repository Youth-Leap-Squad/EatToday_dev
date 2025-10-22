package com.eat.today.event.albti.command.application.service;

import com.eat.today.event.albti.command.application.dto.AlbtiMemberAddBulkRequestDTO;
import com.eat.today.event.albti.command.application.dto.AlbtiMemberAddBulkResponseDTO;
import com.eat.today.event.albti.command.application.entity.AlbtiAnswer;
import com.eat.today.event.albti.command.domain.repository.AlbtiAnswerRepository;
import com.eat.today.event.albti.command.domain.repository.AlbtiMemberRepository;
import com.eat.today.event.albti.command.domain.repository.AlbtiSurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlbtiMemberAddBulkService {

    private final AlbtiAnswerRepository albtiAnswerRepository;
    private final AlbtiMemberRepository albtiMemberRepository;
    private final AlbtiSurveyRepository albtiSurveyRepository;

    @Transactional
    public AlbtiMemberAddBulkResponseDTO saveAllAnswers(AlbtiMemberAddBulkRequestDTO request) {

        // 1. 회원 조회
        var member = albtiMemberRepository.findById(request.getMemberNo())
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

        // 2. 설문 답변 리스트 생성 & 저장
        List<AlbtiAnswer> savedAnswers = request.getAnswers().stream().map(answerDto -> {

            var survey = albtiSurveyRepository.findById(answerDto.getAlbtiSurveyNo())
                    .orElseThrow(() -> new RuntimeException("설문 정보를 찾을 수 없습니다."));

            AlbtiAnswer answer = new AlbtiAnswer();
            answer.setMemberNo(member);
            answer.setAlbtiSurvey(survey);
            answer.setChoice(answerDto.getChoice());

            return albtiAnswerRepository.save(answer);
        }).collect(Collectors.toList());

        // 3. 응답 DTO 변환
        List<AlbtiMemberAddBulkResponseDTO.AlbtiAnswerResponseDTO> responseList = savedAnswers.stream()
                .map(saved -> new AlbtiMemberAddBulkResponseDTO.AlbtiAnswerResponseDTO(
                        saved.getAnswerNo(),
                        saved.getMemberNo().getMemberNo(),
                        saved.getAlbtiSurvey().getAlbtiSurveyNo(),
                        saved.getChoice()
                )).collect(Collectors.toList());

        // 4. 결과 반환
        return new AlbtiMemberAddBulkResponseDTO(
                savedAnswers.size() + "건 설문 참여 기록 저장 완료",
                responseList
        );
    }
}