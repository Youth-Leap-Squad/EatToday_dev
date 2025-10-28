//package com.eat.today.event.worldcup.command.application.controller;
//
//import com.eat.today.event.worldcup.command.application.entity.WorldcupWeeklyRank;
//import com.eat.today.event.worldcup.command.domain.repository.WorldcupWeeklyRankRepository;
//import com.eat.today.event.worldcup.query.dto.WorldcupWeeklyRankDTO;
//import com.eat.today.food.command.domain.repository.FoodPostRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/worldcup")
//@RequiredArgsConstructor
//public class WorldcupRankController {
//
//    private final WorldcupWeeklyRankRepository weeklyRankRepository;
//    private final FoodPostRepository foodPostRepository;
//
//    // ✅ 주간 월드컵 순위 조회 API
//    @GetMapping("/rank")
//    public List<WorldcupWeeklyRankDTO> getWeeklyRank(@RequestParam int alcoholId,
//                                                     @RequestParam int weekNo) {
//
//        List<WorldcupWeeklyRank> list =
//                weeklyRankRepository.findWeeklyRank(weekNo, alcoholId);
//
//        // food_id -> food_name 매핑
//        return list.stream()
//                .map(w -> new WorldcupWeeklyRankDTO(
//                        foodPostRepository.findById(w.getFoodId())
//                                .map(f -> f.getFoodName())
//                                .orElse("Unknown"),
//                        w.getWinCount()
//                ))
//                .collect(Collectors.toList());
//    }
//}