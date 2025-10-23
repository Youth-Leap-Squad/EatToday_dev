package com.eat.today.event.albti.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class AlbtiMemberAddBulkResponseDTO {
    private String message; // 저장 완료 메시지
    private List<AlbtiAnswerResponseDTO> savedAnswers; // 저장된 답변 리스트

    @Getter
    @AllArgsConstructor
    public static class AlbtiAnswerResponseDTO {
        private int answerNo;
        private int memberNo;
        private int albtiSurveyNo;
        private String choice;
    }
}
