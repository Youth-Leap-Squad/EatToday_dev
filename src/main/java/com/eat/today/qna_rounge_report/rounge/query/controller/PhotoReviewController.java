package com.eat.today.qna_rounge_report.rounge.query.controller;

import com.eat.today.qna_rounge_report.rounge.query.dto.PhotoReviewPageResponse;
import com.eat.today.qna_rounge_report.rounge.query.service.PhotoReviewService;
import com.eat.today.configure.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class PhotoReviewController {

    private final PhotoReviewService service;

    public PhotoReviewController(PhotoReviewService service) {
        this.service = service;
    }

    @GetMapping("/photoReview/date")
    public PhotoReviewPageResponse getAllByDateDesc(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        return service.getAllByDateDescPaged(page, size);
    }

    @GetMapping("/photoReview/like")
    public PhotoReviewPageResponse getAllByLikeDesc(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        return service.getAllByLikeDescPaged(page, size);
    }

    @GetMapping("/photoReview/search")
    public PhotoReviewPageResponse search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        return service.searchPaged(keyword, page, size);
    }

    @GetMapping("/photoReview/alcohol")
    public PhotoReviewPageResponse byAlcohol(
            @RequestParam int alcoholNo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        return service.getByAlcoholNoPaged(alcoholNo, page, size);
    }

    @GetMapping("/photoReview/alcohol/like")
    public PhotoReviewPageResponse byAlcoholOrderLike(
            @RequestParam int alcoholNo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        return service.getByAlcoholNoLikeDescPaged(alcoholNo, page, size);
    }

    @GetMapping("/photoReview/member")
    public PhotoReviewPageResponse getByMemberNo(
            @RequestParam int memberNo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        return service.getByMemberNoPaged(memberNo, page, size);
    }

    @GetMapping("/photoReview/member/me")
    public PhotoReviewPageResponse getMyReviews(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        return service.getByMemberNoPaged(user.getMemberNo(), page, size);
    }
}