package com.eat.today.event.albti.command.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AlbtiMemberAddBulkRequestDTO {
    private int memberNo;

    private List<AlbtiAnswerRequestDTO> answers;

    @Getter
    @Setter
    public static class AlbtiAnswerRequestDTO {
        private int albtiSurveyNo;
        private String choice; // 'A' or 'B'
    }
}
