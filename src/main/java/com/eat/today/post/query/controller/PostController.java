package com.eat.today.post.query.controller;

import com.eat.today.post.query.dto.*;
import com.eat.today.post.query.dto.AlcoholDTO;
import com.eat.today.post.query.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class PostController {

    private final PostService postService;

    // ========= 전체 조회 =========

    /** 술 전체 조회 (페이징) */
    @GetMapping("/alcohols")
    public List<AlcoholDTO> getAlcohols(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        return postService.getAlcohols(page, size);
    }

    // ========= alcohol 추가 조회 =========

    /** 술: 설명/사진만 간단 조회 */
    @GetMapping("/alcohols/simple")
    public List<AlcoholSimpleDTO> getAlcoholSimple() {
        return postService.getAlcoholExplainAndPicture();
    }

    // ========= foods 조회 =========

    /** 승인된 안주 조회 */
    @GetMapping("/foods/approved")
    public List<FoodDTO> getApprovedFoods(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return postService.getApprovedFoods(page, size);
    }

    /** 조회수(board_seq) 순 */
    @GetMapping("/foods/sorted/views")
    public List<FoodDTO> byViews(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        return postService.getFoodsSortedByViews(page, size);
    }

    /** 댓글순 */
    @GetMapping("/foods/sorted/comments")
    public List<FoodDTO> byComments(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        return postService.getFoodsSortedByComments(page, size);
    }

    /** 반응(좋아요 합계) 순 */
    @GetMapping("/foods/sorted/likes")
    public List<FoodDTO> byLikes(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        return postService.getFoodsSortedByTotalLikes(page, size);
    }
    /** 승인 안된(미승인) 안주만 조회 */
    @GetMapping("/foods/unapproved")
    public List<FoodDTO> getUnapprovedFoods(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        return postService.getUnapprovedFoods(page, size);
    }

    /** 승인 여부 무관 전체 안주 조회 */
    @GetMapping("/foods/all")
    public List<FoodDTO> getAllFoods(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        return postService.getAllFoods(page, size);
    }

    @GetMapping("/members/{memberNo}/foods")
    public List<MyFoodDTO> getMyFoods(@PathVariable int memberNo,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        return postService.getMyFoods(memberNo, page, size);
    }

    /** 특정 술에 매칭되는 승인된 안주 조회 */
    @GetMapping("/{alcoholNo}/foods")
    public List<FoodDTO> getApprovedFoodsByAlcohol(@PathVariable int alcoholNo,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        return postService.getApprovedFoodsByAlcohol(alcoholNo, page, size);
    }

    // ========= reactions / comments =========

    /** 특정 게시글의 반응(종류별 집계) */
    @GetMapping("/foods/{boardNo}/reactions")
    public List<FoodPostLikesDTO> getFoodReactions(@PathVariable int boardNo) {
        return postService.getFoodPostLikesFromPost(boardNo);
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

    // ========= bookmark =========

    /** 특정 회원의 즐겨찾기 목록 */
    @GetMapping("/bookmarks/{memberNo}")
    public List<BookmarkDTO> getBookmarks(@PathVariable int memberNo) {
        return postService.getBookmarksByMember(memberNo);
    }

    /** 내 북마크 폴더 목록 */
    @GetMapping("/members/{memberNo}/bookmark-folders")
    public List<BookmarkFolderDTO> getBookmarkFolders(@PathVariable int memberNo) {
        return postService.getBookmarkFolders(memberNo);
    }

    /** 특정 폴더의 즐겨찾기 목록 */
    @GetMapping("/bookmark-folders/{folderId}/items")
    public List<BookmarkItemDTO> getBookmarksInFolder(@PathVariable int folderId,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "20") int size) {
        return postService.getBookmarksInFolder(folderId, page, size);
    }

    // ========= 승인/인기 목록 =========

    /** 인기 게시글 TOP N */
    @GetMapping("/foods/popular")
    public List<PopularFoodDTO> getPopularFoods(@RequestParam(defaultValue = "10") int limit) {
        return postService.getPopularFoods(limit);
    }
}
