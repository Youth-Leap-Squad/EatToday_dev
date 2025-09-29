package com.eat.today.qna_rounge_report.rounge.query.controller;

import com.eat.today.qna_rounge_report.rounge.query.dto.PhotoReviewDTO;
import com.eat.today.qna_rounge_report.rounge.query.service.PhotoReviewService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PhotoReviewController {

    private final PhotoReviewService service;

    public PhotoReviewController(PhotoReviewService service) {
        this.service = service;
    }

    @GetMapping("/photoReview/date")
    public List<PhotoReviewDTO> getAllByDateDesc() {
        return service.getAllByDateDesc();
    }

    @GetMapping("/photoReview/like")
    public List<PhotoReviewDTO> getAllByLikeDesc() {
        return service.getAllByLikeDesc();
    }

    @GetMapping("/photoReview/search")
    public List<PhotoReviewDTO> search(@RequestParam String keyword) {
        return service.search(keyword);
    }

    @GetMapping("/photoReview/alcohol")
    public List<PhotoReviewDTO> byAlcohol(@RequestParam int alcoholNo) {
        return service.getByAlcoholNo(alcoholNo);
    }

}
