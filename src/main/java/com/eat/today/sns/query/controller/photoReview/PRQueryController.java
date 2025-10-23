package com.eat.today.sns.query.controller.photoReview;

import com.eat.today.sns.query.dto.photoReview.PRDTO;
import com.eat.today.sns.query.service.photoReview.PRQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/query/photo-reviews")
@RequiredArgsConstructor
public class PRQueryController {

    private final PRQueryService service;

    // 리뷰 상세 + 첨부파일 목록
    @GetMapping("/{reviewNo}")
    public PRDTO getDetail(@PathVariable int reviewNo) {
        return service.getDetail(reviewNo);
    }

    // 게시판별 목록 조회
    @GetMapping
    public List<PRDTO> getListByBoard(@RequestParam int boardNo,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size){
        return service.getListByBoard(boardNo);
    }

    // 멤버별 목록 조회
    @GetMapping("/member/{memberNo}")
    public List<PRDTO> getListByMember(@PathVariable int memberNo,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size){
        return service.getListByMember(memberNo);
    }
}
