package com.eat.today.sns.command.application.controller.photoReview;

import com.eat.today.sns.command.application.dto.photoReviewDTO.CreateRequest;
import com.eat.today.sns.command.application.dto.photoReviewDTO.UpdateRequest;
import com.eat.today.sns.command.domain.service.PhotoReviewCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/command/photo-reviews")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"}, allowCredentials = "true")
public class PhotoReviewCommandController {

    private static final Logger log = LoggerFactory.getLogger(PhotoReviewCommandController.class);
    private final PhotoReviewCommandService service;

    /** 🔐 현재 인증된 사용자의 memberNo 가져오기 */
    private Integer extractMemberNo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;

        try {
            Object principal = auth.getPrincipal();

            // ① CustomUserDetails 안에 getMemberNo()가 있으면
            try {
                var m = principal.getClass().getMethod("getMemberNo");
                Object v = m.invoke(principal);
                if (v != null) return Integer.parseInt(v.toString());
            } catch (NoSuchMethodException ignore) {}

            // ② Authentication#getName()에 memberNo 저장된 경우
            try {
                return Integer.parseInt(auth.getName());
            } catch (NumberFormatException ignore) {}

        } catch (Exception e) {
            log.warn("❗ memberNo 추출 실패: {}", e.getMessage());
        }
        return null;
    }

    /** 🟢 리뷰 등록 (본문 + 이미지 동시) */
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(
            @Valid @RequestPart("review") CreateRequest review,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) throws IOException {
        Integer memberNo = extractMemberNo();
        if (memberNo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "로그인이 필요합니다."));
        }

        // ✅ 프론트에서 온 memberNo 무시하고 토큰에서 세팅
        review.setMemberNo(memberNo);
        if (review.getReviewLike() == null) review.setReviewLike(0);

        int reviewNo = service.create(review, files == null ? List.of() : files);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("reviewNo", reviewNo));
    }

    /** 🟡 리뷰 수정 (본문만 JSON) */
    @PatchMapping(
            path = "/{reviewNo}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editJson(
            @PathVariable int reviewNo,
            @RequestBody UpdateRequest reviewPatch
    ) throws IOException {
        Integer memberNo = extractMemberNo();
        if (memberNo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "로그인이 필요합니다."));
        }
        reviewPatch.setMemberNo(memberNo);

        int updated = service.editWithFiles(reviewNo, reviewPatch, List.of(), List.of());
        return updated > 0
                ? ResponseEntity.ok(Map.of("updated", updated))
                : ResponseEntity.notFound().build();
    }

    /** 🟣 리뷰 수정 (본문 + 파일) */
    @PatchMapping(
            path = "/{reviewNo}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> edit(
            @PathVariable int reviewNo,
            @RequestPart(value = "review", required = false) UpdateRequest reviewPatch,
            @RequestPart(value = "addFiles", required = false) List<MultipartFile> addFiles,
            @RequestPart(value = "deleteFileNos", required = false) List<Integer> deleteFileNos
    ) throws IOException {
        Integer memberNo = extractMemberNo();
        if (memberNo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "로그인이 필요합니다."));
        }
        if (reviewPatch != null) reviewPatch.setMemberNo(memberNo);

        int updated = service.editWithFiles(
                reviewNo,
                reviewPatch,
                addFiles == null ? List.of() : addFiles,
                deleteFileNos == null ? List.of() : deleteFileNos
        );
        return updated > 0
                ? ResponseEntity.ok(Map.of("updated", updated))
                : ResponseEntity.notFound().build();
    }

    /** 🔴 리뷰 삭제 */
    @DeleteMapping("/{reviewNo}")
    public ResponseEntity<?> delete(@PathVariable int reviewNo) {
        Integer memberNo = extractMemberNo();
        if (memberNo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "로그인이 필요합니다."));
        }

        int affected = service.delete(reviewNo);
        return affected > 0
                ? ResponseEntity.ok(Map.of("deleted", affected))
                : ResponseEntity.notFound().build();
    }
}