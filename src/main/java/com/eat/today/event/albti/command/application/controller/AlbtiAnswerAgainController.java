package com.eat.today.event.albti.command.application.controller;

import com.eat.today.event.albti.command.application.dto.AlbtiAnswerAgainRequestDTO;
import com.eat.today.event.albti.command.application.dto.AlbtiAnswerAgainResponceDTO;
import com.eat.today.event.albti.command.application.service.AlbtiAnswerAgainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/albti/answer")
@RequiredArgsConstructor
public class AlbtiAnswerAgainController {
    private final AlbtiAnswerAgainService albtiAnswerAgainService;

    // 재검사 : 술BTI 응답 다시 선택
    @PostMapping("/again")
    public AlbtiAnswerAgainResponceDTO recheckAnswer(@RequestBody AlbtiAnswerAgainRequestDTO requestDTO) {
        return albtiAnswerAgainService.recheckAnswer(requestDTO);
    }
}
