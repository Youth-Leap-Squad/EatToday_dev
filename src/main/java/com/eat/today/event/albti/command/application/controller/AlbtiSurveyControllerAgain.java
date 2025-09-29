package com.eat.today.event.albti.command.application.controller;

import com.eat.today.event.albti.command.application.dto.AlbtiSurveyAgainRequestDTO;
import com.eat.today.event.albti.command.application.dto.AlbtiSurveyAgainResponseDTO;
import com.eat.today.event.albti.command.application.service.AlbtiSurveyServiceAgain;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/albti/survey")
@RequiredArgsConstructor
public class AlbtiSurveyControllerAgain {

    private final AlbtiSurveyServiceAgain albtiSurveyServiceAgain;

//    /**
//     * 이전 검사 응답 재선택
//     */
//    @PostMapping("/again")
//    public ResponseEntity<AlbtiSurveyAgainResponseDTO> reSelectSurvey(
//            @RequestBody AlbtiSurveyAgainRequestDTO dto) {
//
//        AlbtiSurveyAgainResponseDTO response = albtiSurveyServiceAgain.reSelectSurvey(dto);
//        return ResponseEntity.ok(response);
//    }

    // 재검사 : 술BTI 설문 다시 선택
    @PostMapping("/again")
    public AlbtiSurveyAgainResponseDTO recheckSurvey(@RequestBody AlbtiSurveyAgainRequestDTO requestDTO){
        return albtiSurveyServiceAgain.recheckSurvey(requestDTO);
    }


}
