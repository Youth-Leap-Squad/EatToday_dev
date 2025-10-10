package com.eat.today.post.command.application.controller;

import com.eat.today.post.command.application.dto.*;
import com.eat.today.post.command.application.service.PostCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestPart;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/command")
public class PostCommandController {

    private final PostCommandService svc;
    private final PostCommandService postCommandService;

    /* ===== 술 종류 ===== */

    @PostMapping(value = "/alcohols", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AlcoholResponse> createAlcohol(
            @RequestPart("meta") CreateAlcoholRequest req,
            @RequestPart(value="image", required=false) MultipartFile image) {
        AlcoholResponse body = svc.createAlcoholWithImage(req, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PatchMapping(value = "/alcohols/{alcoholNo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AlcoholResponse> updateAlcohol(
            @PathVariable Integer alcoholNo,
            @RequestPart("meta") UpdateAlcoholRequest req,
            @RequestPart(value="image", required=false) MultipartFile image) {
        AlcoholResponse resp = postCommandService.updateAlcoholWithImage(alcoholNo, req, image);
        return ResponseEntity.ok(resp);
    }


    @DeleteMapping("/alcohols/{alcoholNo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAlcohol(@PathVariable Integer alcoholNo) {
        svc.deleteAlcohol(alcoholNo);
    }

    /* ===== 안주(게시글) ===== */

    @PostMapping(value = "/foods", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FoodPostResponse> createPost(
            @RequestPart("meta") CreateFoodPostRequest req,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        FoodPostResponse body = svc.createPostWithImage(req, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PatchMapping(value = "/foods/{boardNo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FoodPostResponse> updatePost(
            @PathVariable Integer boardNo,
            @RequestPart("meta") UpdateFoodPostRequest req,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        FoodPostResponse body = svc.updatePostWithImage(boardNo, req, image);
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
