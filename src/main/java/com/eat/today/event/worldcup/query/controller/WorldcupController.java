package com.eat.today.event.worldcup.query.controller;

import com.eat.today.event.worldcup.query.dto.SelectWorldcupDTO;
import com.eat.today.event.worldcup.query.service.WorldcupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/worldcup")
@RequiredArgsConstructor
public class WorldcupController {
    private final WorldcupService worldcupService;

    // alcoholNo 먼저 선택하고 weekNo 선택
    @GetMapping("/getworldcupresult")
    public List<SelectWorldcupDTO> selectWorldcup(
            @RequestParam String alcoholNo,
            @RequestParam String weekNo) {
        return worldcupService.selectWorldcup(alcoholNo,weekNo);
    }

}
