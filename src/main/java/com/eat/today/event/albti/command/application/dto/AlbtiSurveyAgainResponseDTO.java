package com.eat.today.event.albti.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbtiSurveyAgainResponseDTO {
    private int albtiMemberNo;      // 참여자 PK (albti_join_member PK)
    private int memberNo;           // 회원 번호
    private int albtiSurveyNo;      // 선택한 설문번호
    private int albtiNo;            // 선택한 설문번호에 대한 술BTI번호
    private String albtiCategory;   // 선택한 설문에 해당하는 술BTI 유형
    private String albtiDetail;     // 세부 설명
//    private int albtiSurveyNo;
//    private int albtiNo;
//    private int alBTIMemberNo;
//    private String albtiSurveyContent;
}
