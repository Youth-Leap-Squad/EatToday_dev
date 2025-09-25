package com.eat.today.post.query.controller;

import com.eat.today.post.query.dto.*;
import com.eat.today.post.query.dto.AlcoholDTO;
import com.eat.today.post.query.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping // 루트 경로에서 바로 노출 (/alcohols, /foods, ...)
public class PostController {

    private final PostService postService;

    // ========= 전체 조회 =========

    /** 술 전체 조회 (페이징) */
    @GetMapping("/alcohols")
    public List<AlcoholDTO> getAlcohols(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        return postService.getAlcohols(page, size);
    }

    /** 안주 전체 조회 (페이징) */
    @GetMapping("/foods")
    public List<FoodDTO> getFoods(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return postService.getFoods(page, size);
    }
    // ========= alcohol 추가 조회 =========

    /** 술: 설명/사진만 간단 조회 */
    @GetMapping("/alcohols/simple")
    public List<AlcoholSimpleDTO> getAlcoholSimple() {
        return postService.getAlcoholExplainAndPicture();
    }

    // ========= bookmark =========

    /** 특정 회원의 즐겨찾기 목록 */
    @GetMapping("/bookmarks")
    public List<BookmarkDTO> getBookmarks(@RequestParam int memberNo) {
        return postService.getBookmarksByMember(memberNo);
    }

    // ========= reactions / comments =========

    /** 특정 게시글의 반응(종류별 집계) */
    @GetMapping("/foods/{boardNo}/reactions")
    public List<FoodPostLikesStatDTO> getFoodReactions(@PathVariable int boardNo) {
        return postService.getFoodPostLikeStats(boardNo);
    }

    /** 특정 게시글의 댓글 목록 */
    @GetMapping("/foods/{boardNo}/comments")
    public List<FoodCommentDTO> getFoodComments(@PathVariable int boardNo) {
        return postService.getFoodComments(boardNo);
    }

    /** 내가 쓴 댓글(회원 기준, 최신순) */
    @GetMapping("/members/{memberNo}/comments")
    public List<MyCommentDTO> getMyComments(@PathVariable int memberNo) {
        return postService.getMyComments(memberNo);
    }

    // ========= 승인/인기 목록 =========


    /** 인기 게시글 TOP N */
    @GetMapping("/foods/popular")
    public List<PopularFoodDTO> getPopularFoods(@RequestParam(defaultValue = "10") int limit) {
        return postService.getPopularFoods(limit);
    }
}
