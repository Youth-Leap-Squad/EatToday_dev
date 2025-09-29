package com.eat.today.event.worldcup.command.application.controller;

import com.eat.today.event.worldcup.command.application.dto.WorldcupJoinRequestAddAgainDTO;
import com.eat.today.event.worldcup.command.application.dto.WorldcupJoinResponseAgainDTO;
import com.eat.today.event.worldcup.command.application.service.WorldcupServiceAgain;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/worldcup")
public class WorldcupControllerAgain {

    private final WorldcupServiceAgain worldcupServiceAgain; // final + RequiredArgsConstructor 이용

    @PostMapping("/repick")
    public ResponseEntity<WorldcupJoinResponseAgainDTO> rePick(@RequestBody WorldcupJoinRequestAddAgainDTO dto) {
        WorldcupJoinResponseAgainDTO response = worldcupServiceAgain.rePick(dto);
        return ResponseEntity.ok(response);
    }
}
