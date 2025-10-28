package com.eat.today.event.albti.command.application.controller;

import com.eat.today.event.albti.command.application.service.AlbtiSurveyQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/albti/survey")
@RequiredArgsConstructor
public class AlbtiSurveyQueryController {

    private final AlbtiSurveyQueryService albtiSurveyQueryService;

    @GetMapping("/list")
    public ResponseEntity<?> getSurveyList() {
        return ResponseEntity.ok(albtiSurveyQueryService.getSurveyList());
    }
}