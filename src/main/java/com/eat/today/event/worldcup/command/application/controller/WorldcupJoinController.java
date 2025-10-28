package com.eat.today.event.worldcup.command.application.controller;

import com.eat.today.event.worldcup.command.domain.repository.WorldcupJoinMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/worldcup")
@RequiredArgsConstructor
public class WorldcupJoinController {

    private final WorldcupJoinMemberRepository joinMemberRepository;

    // ✅ 해당 주차 + 해당 술 + 해당 회원 중복 참여 체크 API
    @GetMapping("/check")
    public void checkPlayed(@RequestParam int memberNo, @RequestParam int alcoholId) {
        boolean exists = joinMemberRepository.existsThisWeek(memberNo, alcoholId);
        if (exists) throw new IllegalStateException("already_played");
    }

}