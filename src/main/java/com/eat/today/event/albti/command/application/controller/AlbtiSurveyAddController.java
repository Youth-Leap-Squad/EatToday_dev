package com.eat.today.event.albti.command.application.controller;

import com.eat.today.event.albti.command.application.dto.AlbtiSurveyAddRequestDTO;
import com.eat.today.event.albti.command.application.dto.AlbtiSurveyAddResponseDTO;
import com.eat.today.event.albti.command.application.service.AlbtiSurveyAddService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/albti/survey")
@RequiredArgsConstructor
public class AlbtiSurveyAddController {
    private final AlbtiSurveyAddService albtiSurveyServiceAdd;

    @PostMapping("/add")
    public ResponseEntity<AlbtiSurveyAddResponseDTO> addSurvey(@RequestBody AlbtiSurveyAddRequestDTO requestDTO) {
        AlbtiSurveyAddResponseDTO responseDTO = albtiSurveyServiceAdd.addSurvey(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
