package com.eat.today.event.albti.query.controller;

import com.eat.today.event.albti.query.dto.SelectAlbtiDTO;
import com.eat.today.event.albti.query.service.AlbtiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController   // 반드시 필요
@RequiredArgsConstructor   // final 필드 자동 주입
public class AlbtiController {
    private final AlbtiService albtiService;

    @GetMapping("/albti/getalbtiresult")
    public SelectAlbtiDTO selectAlbti(@RequestParam int member_no) {
        return albtiService.selectAlbti(member_no);
    }

}
