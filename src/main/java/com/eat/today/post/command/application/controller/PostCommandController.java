package com.eat.today.post.command.application.controller;

import com.eat.today.post.command.application.dto.*;
import com.eat.today.post.command.application.service.PostCommandService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.eat.today.configure.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;

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
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
    public void deleteAlcohol(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Integer alcoholNo) {
        if (user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        svc.deleteAlcohol(alcoholNo);
    }

    /* ===== 안주(게시글) ===== */
    @PostMapping(value = "/foods", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPost(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestPart("meta") String metaJson,
            @RequestPart(value = "images", required = false) MultipartFile[] images,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
        try {
            CreateFoodPostRequest req = objectMapper.readValue(metaJson, CreateFoodPostRequest.class);
            req.setMemberNo(user.getMemberNo());

            // ✅ Base64 인라인 이미지 가드
            String content = req.getBoardContent();
            if (content != null && content.contains("data:image/")) {
                return ResponseEntity.unprocessableEntity().body("INLINE_IMAGE_NOT_ALLOWED");
            }

            MultipartFile[] toUse = (images != null && images.length > 0)
                    ? images
                    : (image != null ? new MultipartFile[]{image} : null);

            FoodPostResponse body = svc.createPostWithImages(req, toUse);
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        } catch (Exception e) {
            // ✅ 디버깅 용이하게 오류 메시지 반환
            return ResponseEntity.badRequest().body("BAD_REQUEST: " + e.getMessage());
        }
    }

    @PatchMapping(value = "/foods/{boardNo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePost(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Integer boardNo,
            @RequestPart("meta") String metaJson,
            @RequestPart(value = "images", required = false) MultipartFile[] images,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
        try {
            UpdateFoodPostRequest req = objectMapper.readValue(metaJson, UpdateFoodPostRequest.class);

            if (req.getBoardContent() != null && req.getBoardContent().contains("data:image/")) {
                return ResponseEntity.unprocessableEntity().body("INLINE_IMAGE_NOT_ALLOWED");
            }

            MultipartFile[] toUse = (images != null && images.length > 0)
                    ? images
                    : (image != null ? new MultipartFile[]{image} : null);

            FoodPostResponse body = svc.updatePostWithImages(boardNo, user.getMemberNo(), req, toUse);
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("BAD_REQUEST: " + e.getMessage());
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
    public void cancelPost(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Integer boardNo) {
        if (user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        svc.cancelPost(boardNo, user.getMemberNo());
    }

    /** (관리자) 승인 수정: approved=true|false */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/foods/{boardNo}/approve")
    public ResponseEntity<FoodPostResponse> approve(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Integer boardNo,
            @RequestParam boolean approved
    ) {
        FoodPostResponse body = svc.approve(boardNo, approved);
        return ResponseEntity.ok(body);
    }

    /* ===== 댓글 ===== */
    @PostMapping("/foods/{boardNo}/comments")
    public ResponseEntity<?> addCommentOnFood(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Integer boardNo,
            @RequestBody CreateCommentOnFoodRequest req
    ) {
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
        AddCommentRequest delegate = new AddCommentRequest();
        delegate.setBoardNo(boardNo);
        delegate.setMemberNo(user.getMemberNo());
        delegate.setContent(req.getContent());
        CommentResponse resp = svc.addComment(delegate);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> updateCommentById(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Integer commentId,
            @RequestBody UpdateCommentRequest req
    ) {
        CommentResponse resp = svc.updateCommentById(commentId, user.getMemberNo(), req.getContent());
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Integer commentId
    ) {
        svc.deleteCommentById(commentId, user.getMemberNo());
    }

    /* ===== 반응 ===== */
    @PostMapping("/foods/{boardNo}/reactions")
    public ResponseEntity<?> addReaction(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Integer boardNo,
            @RequestBody ReactRequest req
    ) {
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
        req.setMemberNo(user.getMemberNo());
        ReactionResponse body = svc.addReaction(boardNo, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PatchMapping("/foods/{boardNo}/reactions")
    public ResponseEntity<?> changeReaction(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Integer boardNo,
            @RequestBody ReactRequest req
    ) {
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
        req.setMemberNo(user.getMemberNo());
        ReactionResponse body = svc.changeReaction(boardNo, req);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/foods/{boardNo}/reactions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReaction(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Integer boardNo
    ) {
        if (user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        svc.deleteReaction(boardNo, user.getMemberNo());
    }

    /* ===== 즐겨찾기 ===== */
    @PostMapping("/bookmarks/folders")
    @ResponseStatus(HttpStatus.CREATED)
    public void createFolder(@AuthenticationPrincipal CustomUserDetails user, @RequestBody CreateFolderRequest req) {
        if (user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        svc.createFolder(user.getMemberNo(), req.getFolderName());
    }

    @PatchMapping("/bookmarks/folders/{folderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void renameFolder(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Integer folderId,
            @RequestBody RenameFolderRequest req
    ) {
        if (user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        svc.renameFolder(user.getMemberNo(), folderId, req.getFolderName());
    }

    @DeleteMapping("/bookmarks/folders/{folderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFolder(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Integer folderId) {
        if (user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        svc.deleteFolder(user.getMemberNo(), folderId);
    }

    @PostMapping("/bookmarks")
    @ResponseStatus(HttpStatus.CREATED)
    public void addBookmark(@AuthenticationPrincipal CustomUserDetails user, @RequestBody AddBookmarkToFolderRequest req) {
        if (user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        svc.addBookmarkToFolder(user.getMemberNo(), req.getFolderId(), req.getBoardNo());
    }

    @DeleteMapping("/bookmarks")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBookmark(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam Integer folderId,
            @RequestParam Integer boardNo
    ) {
        if (user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        svc.removeBookmarkFromFolder(user.getMemberNo(), folderId, boardNo);
    }

    @PatchMapping("/foods/{boardNo}/view")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void increaseView(@PathVariable Integer boardNo) {
        svc.increaseView(boardNo);
    }
}
