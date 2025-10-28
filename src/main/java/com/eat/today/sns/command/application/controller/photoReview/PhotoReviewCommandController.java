package com.eat.today.sns.command.application.controller.photoReview;

import com.eat.today.sns.command.application.dto.photoReviewDTO.CreateRequest;
import com.eat.today.sns.command.application.dto.photoReviewDTO.UpdateRequest;
import com.eat.today.sns.command.application.entity.photoReview.PhotoReviewEntity;
import com.eat.today.sns.command.domain.repository.photoReview.PhotoReviewRepository;
import com.eat.today.sns.command.domain.service.PhotoReviewCommandService;
import com.eat.today.sns.command.domain.service.PhotoReviewLikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/command/photo-reviews")
@CrossOrigin(origins = {"http://localhost:5173"}, allowCredentials = "true")
public class PhotoReviewCommandController {

    private static final Logger log = LoggerFactory.getLogger(PhotoReviewCommandController.class);

    private final PhotoReviewLikeService likeService;
    private final PhotoReviewCommandService service;
    private final PhotoReviewRepository reviewRepo; // ✅ 좋아요 카운트 업데이트용

    /** 🔐 현재 인증된 사용자의 memberNo 가져오기 */
    private Integer extractMemberNo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;
        try {
            Object principal = auth.getPrincipal();
            // ① CustomUserDetails#getMemberNo()
            try {
                var m = principal.getClass().getMethod("getMemberNo");
                Object v = m.invoke(principal);
                if (v != null) return Integer.parseInt(v.toString());
            } catch (NoSuchMethodException ignore) {}
            // ② Authentication#getName() 저장 형태
            try { return Integer.parseInt(auth.getName()); } catch (NumberFormatException ignore) {}
        } catch (Exception e) {
            log.warn("❗ memberNo 추출 실패: {}", e.getMessage());
        }
        return null;
    }

    /* =========================
       CREATE (본문 + 파일)
     ========================= */
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
        review.setMemberNo(memberNo);               // ✅ 토큰 기준으로 강제
        if (review.getReviewLike() == null) review.setReviewLike(0);

        int reviewNo = service.create(review, files == null ? List.of() : files);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("reviewNo", reviewNo));
    }

    /* =========================
       UPDATE (본문만 JSON)
     ========================= */
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

    /* =========================
       UPDATE (본문 + 파일)
     ========================= */
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

    /* =========================
       DELETE
     ========================= */
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

    /* =========================
       LIKE (toggle with single column)
     ========================= */

    /** 👍 현재 좋아요 수 조회 */
    @GetMapping(value = "/{reviewNo}/likes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLikes(@PathVariable int reviewNo) {
        return reviewRepo.findById(reviewNo)
                .<ResponseEntity<?>>map(e ->
                        ResponseEntity.ok(Map.of(
                                "reviewNo", e.getReviewNo(),
                                "likeCount", Objects.requireNonNullElse(e.getReviewLike(), 0)
                        )))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "리뷰를 찾을 수 없습니다.")));
    }

    /**
     * ❤️ 토글(+/-1)
     * - 요청 바디에 liked(boolean)가 있으면:
     *     liked=true  → 취소(dec) = -1
     *     liked=false → 좋아요(inc) = +1
     * - 없으면 기본적으로 +1 처리
     * - 최소 0 보장
     */
    @PostMapping(value = "/{reviewNo}/likes", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<?> toggleLike(
            @PathVariable int reviewNo,
            @RequestBody(required = false) Map<String, Object> body
    ) {
        // (선택) 인증 필요 시 아래 주석 해제
        // Integer memberNo = extractMemberNo();
        // if (memberNo == null) {
        //     return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        //             .body(Map.of("error", "로그인이 필요합니다."));
        // }

        var opt = reviewRepo.findById(reviewNo);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "리뷰를 찾을 수 없습니다."));
        }

        PhotoReviewEntity e = opt.get();
        int current = Objects.requireNonNullElse(e.getReviewLike(), 0);

        // 클라이언트가 보내는 liked 기준으로 증감 결정
        int delta = +1; // 기본 증가
        if (body != null && body.containsKey("liked")) {
            Object v = body.get("liked");
            boolean liked = v instanceof Boolean ? (Boolean) v :
                    (v instanceof String ? Boolean.parseBoolean((String) v) : false);
            // liked=true(이미 눌러진 상태) → 취소(-1)
            delta = liked ? -1 : +1;
        }

        int next = Math.max(0, current + delta);
        e.setReviewLike(next);
        reviewRepo.save(e);

        // 응답에 liked 추정값도 함께 반환(프론트가 반영하기 쉬움)
        boolean likedAfter = (delta > 0);
        return ResponseEntity.ok(Map.of(
                "reviewNo", e.getReviewNo(),
                "likeCount", next,
                "liked", likedAfter
        ));
    }
}