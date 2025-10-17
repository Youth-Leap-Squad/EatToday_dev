package com.eat.today.post.command.application.controller;

import com.eat.today.post.command.application.dto.*;
import com.eat.today.post.command.application.service.PostCommandService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/command")
public class PostCommandController {

    private final PostCommandService svc;
    private final PostCommandService postCommandService;
    private final ObjectMapper objectMapper; // meta 문자열 파싱

    /* ===== 술 종류 ===== */

    @PostMapping(value = "/alcohols", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AlcoholResponse> createAlcohol(
            @RequestPart("meta") String metaJson,
            @RequestPart(value="image", required=false) MultipartFile image) {
        try {
            CreateAlcoholRequest req = objectMapper.readValue(metaJson, CreateAlcoholRequest.class);
            AlcoholResponse body = svc.createAlcoholWithImage(req, image);
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping(value = "/alcohols/{alcoholNo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AlcoholResponse> updateAlcohol(
            @PathVariable Integer alcoholNo,
            @RequestPart("meta") String metaJson,
            @RequestPart(value="image", required=false) MultipartFile image) {
        try {
            UpdateAlcoholRequest req = objectMapper.readValue(metaJson, UpdateAlcoholRequest.class);
            AlcoholResponse resp = postCommandService.updateAlcoholWithImage(alcoholNo, req, image);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/alcohols/{alcoholNo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAlcohol(@PathVariable Integer alcoholNo) {
        svc.deleteAlcohol(alcoholNo);
    }

    /* ===== 안주(게시글) ===== */

    @PostMapping(value = "/foods", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FoodPostResponse> createPost(
            @RequestPart("meta") String metaJson,
            @RequestPart(value = "images", required = false) MultipartFile[] images,    // 본문용 여러 장
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage // 대표 이미지 1장
    ) {
        try {
            CreateFoodPostRequest req = objectMapper.readValue(metaJson, CreateFoodPostRequest.class);

            // 1) 대표 이미지 저장 → foodPicture(대표) 세팅
            String coverUrl = svc.storeCoverForFood(coverImage);
            if (coverUrl != null) req.setFoodPicture(coverUrl);

            // 2) 본문 이미지 저장 → foodExplain 안에 <img> 삽입
            String withImages = svc.appendImagesToExplain(req.getFoodExplain(), images);
            req.setFoodExplain(withImages);

            FoodPostResponse body = svc.createPost(req);
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping(value = "/foods/{boardNo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FoodPostResponse> updatePost(
            @PathVariable Integer boardNo,
            @RequestPart("meta") String metaJson,
            @RequestPart(value = "images", required = false) MultipartFile[] images,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage
    ) {
        try {
            UpdateFoodPostRequest req = objectMapper.readValue(metaJson, UpdateFoodPostRequest.class);

            // 대표 이미지 교체가 온 경우에만 교체
            String coverUrl = svc.storeCoverForFood(coverImage);
            if (coverUrl != null) req.setFoodPicture(coverUrl);

            // 본문 이미지 추가 삽입 (본문 유지 + 추가)
            String mergedExplain = svc.appendImagesToExplain(req.getFoodExplain(), images);
            req.setFoodExplain(mergedExplain);

            FoodPostResponse body = svc.updatePost(boardNo, req);
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
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


