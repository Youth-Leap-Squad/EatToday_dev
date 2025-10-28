package com.eat.today.post.command.application.controller;

import com.eat.today.post.command.application.dto.*;
import com.eat.today.post.command.application.service.PostCommandService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.eat.today.configure.security.CustomUserDetails;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/command")
public class PostCommandController {

    private final PostCommandService svc;
    private final ObjectMapper objectMapper;

    /* ===== 술 종류 (ADMIN 전용) ===== */

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/alcohols", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AlcoholResponse> createAlcohol(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestPart("meta") String metaJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        try {
            CreateAlcoholRequest req = objectMapper.readValue(metaJson, CreateAlcoholRequest.class);
            AlcoholResponse body = svc.createAlcoholWithImage(req, image);
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = "/alcohols/{alcoholNo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AlcoholResponse> updateAlcohol(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Integer alcoholNo,
            @RequestPart("meta") String metaJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        try {
            UpdateAlcoholRequest req = objectMapper.readValue(metaJson, UpdateAlcoholRequest.class);
            AlcoholResponse resp = svc.updateAlcoholWithImage(alcoholNo, req, image);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/alcohols/{alcoholNo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAlcohol(@AuthenticationPrincipal CustomUserDetails user,
                              @PathVariable Integer alcoholNo) {
        svc.deleteAlcohol(alcoholNo);
    }

    /* ===== 안주(게시글) ===== */

    @PostMapping(value = "/foods", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FoodPostResponse> createPost(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestPart("meta") String metaJson,
            @RequestPart(value = "images", required = false) MultipartFile[] images,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        try {
            CreateFoodPostRequest req = objectMapper.readValue(metaJson, CreateFoodPostRequest.class);
            req.setMemberNo(user.getMemberNo());

            MultipartFile[] toUse = (images != null) ? images : (image != null ? new MultipartFile[]{image} : null);
            FoodPostResponse body = svc.createPostWithImages(req, toUse);
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping(value = "/foods/{boardNo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FoodPostResponse> updatePost(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Integer boardNo,
            @RequestPart("meta") String metaJson,
            @RequestPart(value = "images", required = false) MultipartFile[] images,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        try {
            UpdateFoodPostRequest req = objectMapper.readValue(metaJson, UpdateFoodPostRequest.class);
            MultipartFile[] toUse = (images != null) ? images : (image != null ? new MultipartFile[]{image} : null);
            FoodPostResponse body = svc.updatePostWithImages(boardNo, user.getMemberNo(), req, toUse); // <<< 변경
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/foods/{boardNo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Integer boardNo) {
        svc.deletePost(boardNo);
    }

    @DeleteMapping("/foods/{boardNo}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelPost(@AuthenticationPrincipal CustomUserDetails user,
                           @PathVariable Integer boardNo) {
        svc.cancelPost(boardNo, user.getMemberNo());
    }

    /** (관리자) 승인 수정: approved=true|false */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/foods/{boardNo}/approve")
    public ResponseEntity<FoodPostResponse> approve(@AuthenticationPrincipal CustomUserDetails user,
                                                    @PathVariable Integer boardNo,
                                                    @RequestParam boolean approved) {
        FoodPostResponse body = svc.approve(boardNo, approved);
        return ResponseEntity.ok(body);
    }

    /* ===== 댓글 ===== */

    @PostMapping("/foods/{boardNo}/comments")
    public ResponseEntity<CommentResponse> addCommentOnFood(@AuthenticationPrincipal CustomUserDetails user,
                                                            @PathVariable Integer boardNo,
                                                            @RequestBody CreateCommentOnFoodRequest req) {
        AddCommentRequest delegate = new AddCommentRequest();
        delegate.setBoardNo(boardNo);
        delegate.setMemberNo(user.getMemberNo());
        delegate.setContent(req.getContent());

        CommentResponse resp = svc.addComment(delegate);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> updateCommentById(@AuthenticationPrincipal CustomUserDetails user,
                                                             @PathVariable("commentId") Integer commentId,
                                                             @RequestBody UpdateCommentRequest req) {
        CommentResponse resp = svc.updateCommentById(commentId, user.getMemberNo(), req.getContent());
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("/comments/{foodCommentNo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@AuthenticationPrincipal CustomUserDetails user,
                                  @PathVariable("foodCommentNo") Integer commentId) {
        svc.deleteCommentById(commentId, user.getMemberNo());
    }

    /* ===== 반응 ===== */

    @PostMapping("/foods/{boardNo}/reactions")
    public ResponseEntity<ReactionResponse> addReaction(@AuthenticationPrincipal CustomUserDetails user,
                                                        @PathVariable Integer boardNo,
                                                        @RequestBody ReactRequest req) {
        req.setMemberNo(user.getMemberNo());
        ReactionResponse body = svc.addReaction(boardNo, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PatchMapping("/foods/{boardNo}/reactions")
    public ResponseEntity<ReactionResponse> changeReaction(@AuthenticationPrincipal CustomUserDetails user,
                                                           @PathVariable Integer boardNo,
                                                           @RequestBody ReactRequest req) {
        req.setMemberNo(user.getMemberNo());
        ReactionResponse body = svc.changeReaction(boardNo, req);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/foods/{boardNo}/reactions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReaction(@AuthenticationPrincipal CustomUserDetails user,
                               @PathVariable Integer boardNo) {
        svc.deleteReaction(boardNo, user.getMemberNo());
    }

    /* ===== 즐겨찾기 ===== */

    // 폴더 생성
    @PostMapping("/bookmarks/folders")
    @ResponseStatus(HttpStatus.CREATED)
    public void createFolder(@AuthenticationPrincipal CustomUserDetails user,
                             @RequestBody CreateFolderRequest req) {
        svc.createFolder(user.getMemberNo(), req.getFolderName());
    }

    // 폴더명 변경
    @PatchMapping("/bookmarks/folders/{folderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void renameFolder(@AuthenticationPrincipal CustomUserDetails user,
                             @PathVariable Integer folderId,
                             @RequestBody RenameFolderRequest req) {
        svc.renameFolder(user.getMemberNo(), folderId, req.getFolderName());
    }

    // 폴더 삭제
    @DeleteMapping("/bookmarks/folders/{folderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFolder(@AuthenticationPrincipal CustomUserDetails user,
                             @PathVariable Integer folderId) {
        svc.deleteFolder(user.getMemberNo(), folderId);
    }

    // 폴더에 즐겨찾기 추가
    @PostMapping("/bookmarks")
    @ResponseStatus(HttpStatus.CREATED)
    public void addBookmark(@AuthenticationPrincipal CustomUserDetails user,
                            @RequestBody AddBookmarkToFolderRequest req) {
        svc.addBookmarkToFolder(user.getMemberNo(), req.getFolderId(), req.getBoardNo());
    }

    // 폴더에서 즐겨찾기 제거
    @DeleteMapping("/bookmarks")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBookmark(@AuthenticationPrincipal CustomUserDetails user,
                               @RequestParam Integer folderId,
                               @RequestParam Integer boardNo) {
        svc.removeBookmarkFromFolder(user.getMemberNo(), folderId, boardNo);
    }

    // 즐겨찾기 이동
    @PostMapping("/bookmarks/move")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void moveBookmark(@AuthenticationPrincipal CustomUserDetails user,
                             @RequestBody MoveBookmarkRequest req) {
        svc.moveBookmark(user.getMemberNo(), req.getFromFolderId(), req.getToFolderId(), req.getBoardNo());
    }

    @PatchMapping("/foods/{boardNo}/view")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void increaseView(@PathVariable Integer boardNo) {
        svc.increaseView(boardNo);
    }
}
