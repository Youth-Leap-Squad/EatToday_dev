package com.eat.today.event.worldcup.query.controller;

import com.eat.today.event.worldcup.query.dto.SelectWorldcupDTO;
import com.eat.today.event.worldcup.query.service.WorldcupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class WorldcupController {
    private final WorldcupService worldcupService;

    @GetMapping("/worldcup/getworldcupresult")
    public List<SelectWorldcupDTO> selectWorldcup(@RequestParam String weekNo) {
        return worldcupService.selectWorldcup(weekNo);
    }

}
