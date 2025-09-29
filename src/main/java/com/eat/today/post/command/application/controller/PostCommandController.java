package com.eat.today.post.command.application.controller;

import com.eat.today.post.command.application.dto.*;
import com.eat.today.post.command.application.service.PostCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/command")
public class PostCommandController {

    private final PostCommandService svc;
    private final PostCommandService postCommandService;

    /* ===== 술 종류 ===== */

    @PostMapping("/alcohols")
    public ResponseEntity<AlcoholResponse> createAlcohol(@RequestBody CreateAlcoholRequest req) {
        AlcoholResponse body = svc.createAlcohol(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PatchMapping("/alcohols/{alcoholNo}")
    public ResponseEntity<AlcoholResponse> updateAlcohol(@PathVariable Integer alcoholNo,
                                                         @RequestBody UpdateAlcoholRequest req) {
        AlcoholResponse resp = postCommandService.updateAlcohol(alcoholNo, req);
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("/alcohols/{alcoholNo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAlcohol(@PathVariable Integer alcoholNo) {
        svc.deleteAlcohol(alcoholNo);
    }

    /* ===== 안주(게시글) ===== */

    @PostMapping("/foods")
    public ResponseEntity<FoodPostResponse> createPost(@RequestBody CreateFoodPostRequest req) {
        FoodPostResponse body = svc.createPost(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PatchMapping("/foods/{boardNo}")
    public ResponseEntity<FoodPostResponse> updatePost(@PathVariable Integer boardNo,
                                                       @RequestBody UpdateFoodPostRequest req) {
        FoodPostResponse body = svc.updatePost(boardNo, req);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/foods/{boardNo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Integer boardNo) {
        svc.deletePost(boardNo);
    }

    @DeleteMapping("/foods/{boardNo}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelPost(@PathVariable Integer boardNo,
                           @RequestParam Integer memberNo) {
        svc.cancelPost(boardNo, memberNo);
    }

    /** (관리자) 승인 수정: approved=true|false */
    @PatchMapping("/foods/{boardNo}/approve")
    public ResponseEntity<FoodPostResponse> approve(@PathVariable Integer boardNo,
                                                    @RequestParam boolean approved) {
        FoodPostResponse body = svc.approve(boardNo, approved);
        return ResponseEntity.ok(body);
    }

    /* ===== 댓글 ===== */

    @PostMapping("/foods/{boardNo}/comments")
    public ResponseEntity<CommentResponse> addCommentOnFood(@PathVariable Integer boardNo,
                                                            @RequestBody CreateCommentOnFoodRequest req) {

        AddCommentRequest delegate = new AddCommentRequest();
        delegate.setBoardNo(boardNo);
        delegate.setMemberNo(req.getMemberNo());
        delegate.setContent(req.getContent());

        CommentResponse resp = svc.addComment(delegate);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> updateCommentById(@PathVariable("commentId") Integer commentId,
                                                             @RequestBody UpdateCommentRequest req) {
        CommentResponse resp = svc.updateCommentById(commentId, req.getMemberNo(), req.getContent());
        return ResponseEntity.ok(resp);
    }


    @DeleteMapping("/comments/{foodCommentNo}")
    public void deleteCommentById(@PathVariable("foodCommentNo") Integer commentId,
                                  @RequestParam Integer memberNo) {
        postCommandService.deleteCommentById(commentId, memberNo);
    }

    /* ===== 반응 ===== */

    @PostMapping("/foods/{boardNo}/reactions")
    public ResponseEntity<ReactionResponse> addReaction(@PathVariable Integer boardNo,
                                                        @RequestBody ReactRequest req) {
        ReactionResponse body = svc.addReaction(boardNo, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PatchMapping("/foods/{boardNo}/reactions")
    public ResponseEntity<ReactionResponse> changeReaction(@PathVariable Integer boardNo,
                                                           @RequestBody ReactRequest req) {
        ReactionResponse body = svc.changeReaction(boardNo, req);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/foods/{boardNo}/reactions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReaction(@PathVariable Integer boardNo,
                               @RequestParam Integer memberNo) {
        svc.deleteReaction(boardNo, memberNo);
    }

    /* ===== 즐겨찾기 ===== */

    @PostMapping("/bookmarks")
    public ResponseEntity<List<BookmarkResponse>> addBookmark(@RequestBody AddBookmarkRequest req) {
        List<BookmarkResponse> body = svc.addBookmark(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @DeleteMapping("/bookmarks")
    public ResponseEntity<List<BookmarkResponse>> removeBookmark(@RequestParam Integer memberNo,
                                                                 @RequestParam Integer boardNo) {
        List<BookmarkResponse> body = svc.removeBookmark(memberNo, boardNo);
        return ResponseEntity.ok(body);
    }

}
