package com.eat.today.event.albti.command.application.controller;


import com.eat.today.event.albti.command.application.dto.AlbtiSurveyAddRequestDTO;
import com.eat.today.event.albti.command.application.dto.AlbtiSurveyAddResponseDTO;
import com.eat.today.event.albti.command.application.service.AlbtiSurveyServiceAdd;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/albti/survey")
@RequiredArgsConstructor
public class AlbtiSurveyControllerAdd {

    private final AlbtiSurveyServiceAdd albtiSurveyService;

//    @PostMapping("/add")
//    public String addSurvey(@RequestBody SurveyRequestDTO requestDTO) {
//        albtiSurveyService.addSurvey(
//                requestDTO.getAlbtiNo(),
//                requestDTO.getAlbtiSurveyContent()
//        );
//
//        return "술BTI 설문 내용 추가가 완료되었습니다!";
//    }

    @PostMapping("/add")
    public ResponseEntity<AlbtiSurveyAddResponseDTO> addSurvey(@RequestBody AlbtiSurveyAddRequestDTO requestDTO) {
        AlbtiSurveyAddResponseDTO responseDTO = albtiSurveyService.addSurvey(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

}
