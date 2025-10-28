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
    private final PhotoReviewRepository reviewRepo;

    /* =========================
       인증 유틸리티
     ========================= */

    /**
     * 🔐 현재 인증된 사용자의 memberNo 가져오기
     * @return memberNo 또는 null (인증되지 않은 경우)
     */
    private Integer extractMemberNo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }

        try {
            Object principal = auth.getPrincipal();

            // ① CustomUserDetails#getMemberNo() 시도
            try {
                var method = principal.getClass().getMethod("getMemberNo");
                Object value = method.invoke(principal);
                if (value != null) {
                    return Integer.parseInt(value.toString());
                }
            } catch (NoSuchMethodException e) {
                // 메서드 없음 - 다음 방법 시도
            }

            // ② Authentication#getName()에서 파싱 시도
            try {
                String name = auth.getName();
                if (name != null && !name.equals("anonymousUser")) {
                    return Integer.parseInt(name);
                }
            } catch (NumberFormatException e) {
                // 숫자가 아님 - 실패
            }

        } catch (Exception e) {
            log.warn("❗ memberNo 추출 실패: {}", e.getMessage(), e);
        }

        return null;
    }

    /**
     * 인증 실패 응답 생성
     */
    private ResponseEntity<?> unauthorizedResponse() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "error", "로그인이 필요합니다.",
                        "message", "Authentication required"
                ));
    }

    /* =========================
       CREATE (본문 + 파일)
     ========================= */

    /**
     * 📝 사진 리뷰 생성
     * @param review 리뷰 정보 (JSON)
     * @param files 첨부 파일 목록
     * @return 생성된 리뷰 번호
     */
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(
            @Valid @RequestPart("review") CreateRequest review,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) throws IOException {

        // 인증 확인
        Integer memberNo = extractMemberNo();
        if (memberNo == null) {
            return unauthorizedResponse();
        }

        // memberNo 강제 설정 (토큰 기준)
        review.setMemberNo(memberNo);

        // 초기 좋아요 수 설정
        if (review.getReviewLike() == null) {
            review.setReviewLike(0);
        }

        // 리뷰 생성
        int reviewNo = service.create(review, files == null ? List.of() : files);

        log.info("✅ 리뷰 생성 완료 - reviewNo: {}, memberNo: {}", reviewNo, memberNo);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "reviewNo", reviewNo,
                        "message", "리뷰가 성공적으로 생성되었습니다."
                ));
    }

    /* =========================
       UPDATE (본문만 JSON)
     ========================= */

    /**
     * 📝 리뷰 수정 (JSON 본문만)
     * @param reviewNo 리뷰 번호
     * @param reviewPatch 수정할 내용
     * @return 업데이트 결과
     */
    @PatchMapping(
            path = "/{reviewNo}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editJson(
            @PathVariable int reviewNo,
            @RequestBody UpdateRequest reviewPatch
    ) throws IOException {

        // 인증 확인
        Integer memberNo = extractMemberNo();
        if (memberNo == null) {
            return unauthorizedResponse();
        }

        // 권한 확인 (작성자만 수정 가능)
        if (!isOwner(reviewNo, memberNo)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "error", "권한이 없습니다.",
                            "message", "본인이 작성한 리뷰만 수정할 수 있습니다."
                    ));
        }

        reviewPatch.setMemberNo(memberNo);

        int updated = service.editWithFiles(reviewNo, reviewPatch, List.of(), List.of());

        if (updated > 0) {
            log.info("✅ 리뷰 수정 완료 - reviewNo: {}, memberNo: {}", reviewNo, memberNo);
            return ResponseEntity.ok(Map.of(
                    "updated", updated,
                    "message", "리뷰가 수정되었습니다."
            ));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", "리뷰를 찾을 수 없습니다.",
                            "reviewNo", reviewNo
                    ));
        }
    }

    /* =========================
       UPDATE (본문 + 파일)
     ========================= */

    /**
     * 📝 리뷰 수정 (파일 포함)
     * @param reviewNo 리뷰 번호
     * @param reviewPatch 수정할 내용
     * @param addFiles 추가할 파일 목록
     * @param deleteFileNos 삭제할 파일 번호 목록
     * @return 업데이트 결과
     */
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

        // 인증 확인
        Integer memberNo = extractMemberNo();
        if (memberNo == null) {
            return unauthorizedResponse();
        }

        // 권한 확인
        if (!isOwner(reviewNo, memberNo)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "error", "권한이 없습니다.",
                            "message", "본인이 작성한 리뷰만 수정할 수 있습니다."
                    ));
        }

        if (reviewPatch != null) {
            reviewPatch.setMemberNo(memberNo);
        }

        int updated = service.editWithFiles(
                reviewNo,
                reviewPatch,
                addFiles == null ? List.of() : addFiles,
                deleteFileNos == null ? List.of() : deleteFileNos
        );

        if (updated > 0) {
            log.info("✅ 리뷰 수정 완료 (파일 포함) - reviewNo: {}, memberNo: {}", reviewNo, memberNo);
            return ResponseEntity.ok(Map.of(
                    "updated", updated,
                    "message", "리뷰가 수정되었습니다."
            ));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", "리뷰를 찾을 수 없습니다.",
                            "reviewNo", reviewNo
                    ));
        }
    }

    /* =========================
       DELETE
     ========================= */

    /**
     * 🗑️ 리뷰 삭제
     * @param reviewNo 리뷰 번호
     * @return 삭제 결과
     */
    @DeleteMapping("/{reviewNo}")
    public ResponseEntity<?> delete(@PathVariable int reviewNo) {

        // 인증 확인
        Integer memberNo = extractMemberNo();
        if (memberNo == null) {
            return unauthorizedResponse();
        }

        // 권한 확인
        if (!isOwner(reviewNo, memberNo)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "error", "권한이 없습니다.",
                            "message", "본인이 작성한 리뷰만 삭제할 수 있습니다."
                    ));
        }

        int affected = service.delete(reviewNo);

        if (affected > 0) {
            log.info("✅ 리뷰 삭제 완료 - reviewNo: {}, memberNo: {}", reviewNo, memberNo);
            return ResponseEntity.ok(Map.of(
                    "deleted", affected,
                    "message", "리뷰가 삭제되었습니다."
            ));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", "리뷰를 찾을 수 없습니다.",
                            "reviewNo", reviewNo
                    ));
        }
    }

    /* =========================
       LIKE (좋아요)
     ========================= */

    /**
     * 👍 현재 좋아요 수 조회
     * @param reviewNo 리뷰 번호
     * @return 좋아요 수
     */
    @GetMapping(value = "/{reviewNo}/likes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLikes(@PathVariable int reviewNo) {
        return reviewRepo.findById(reviewNo)
                .<ResponseEntity<?>>map(entity ->
                        ResponseEntity.ok(Map.of(
                                "reviewNo", entity.getReviewNo(),
                                "likeCount", Objects.requireNonNullElse(entity.getReviewLike(), 0)
                        )))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "리뷰를 찾을 수 없습니다.")));
    }

    /**
     * ❤️ 좋아요 토글 (증가/감소)
     *
     * 요청 바디:
     * - liked: true (이미 좋아요 눌린 상태) → 취소 (-1)
     * - liked: false (좋아요 안 눌린 상태) → 추가 (+1)
     * - memberNo: 사용자 번호 (선택사항, 인증된 경우 자동 추출)
     *
     * @param reviewNo 리뷰 번호
     * @param body 요청 바디
     * @return 업데이트된 좋아요 수와 상태
     */
    @PostMapping(value = "/{reviewNo}/likes", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<?> toggleLike(
            @PathVariable int reviewNo,
            @RequestBody(required = false) Map<String, Object> body
    ) {

        // (선택) 인증 필요 시 아래 주석 해제
        Integer memberNo = extractMemberNo();
        if (memberNo == null) {
            return unauthorizedResponse();
        }

        // 리뷰 조회
        var optionalReview = reviewRepo.findById(reviewNo);
        if (optionalReview.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "리뷰를 찾을 수 없습니다."));
        }

        PhotoReviewEntity entity = optionalReview.get();
        int currentLikeCount = Objects.requireNonNullElse(entity.getReviewLike(), 0);

        // 클라이언트가 보낸 liked 값에 따라 증감 결정
        int delta = +1; // 기본: 좋아요 추가
        boolean likedBefore = false;

        if (body != null && body.containsKey("liked")) {
            Object value = body.get("liked");
            likedBefore = parseBoolean(value);

            // liked=true (이미 좋아요 눌림) → 취소 (-1)
            // liked=false (좋아요 안 눌림) → 추가 (+1)
            delta = likedBefore ? -1 : +1;
        }

        // 새로운 좋아요 수 계산 (최소 0)
        int newLikeCount = Math.max(0, currentLikeCount + delta);
        entity.setReviewLike(newLikeCount);
        reviewRepo.save(entity);

        // 좋아요 후 상태
        boolean likedAfter = delta > 0;

        log.info("✅ 좋아요 토글 - reviewNo: {}, memberNo: {}, {} → {}, likeCount: {} → {}",
                reviewNo, memberNo, likedBefore, likedAfter, currentLikeCount, newLikeCount);

        return ResponseEntity.ok(Map.of(
                "reviewNo", entity.getReviewNo(),
                "likeCount", newLikeCount,
                "liked", likedAfter,
                "message", likedAfter ? "좋아요를 눌렀습니다." : "좋아요를 취소했습니다."
        ));
    }

    /* =========================
       헬퍼 메서드
     ========================= */

    /**
     * 리뷰 소유자 확인
     * @param reviewNo 리뷰 번호
     * @param memberNo 회원 번호
     * @return 소유자 여부
     */
    private boolean isOwner(int reviewNo, Integer memberNo) {
        if (memberNo == null) {
            return false;
        }

        return reviewRepo.findById(reviewNo)
                .map(entity -> entity.getMemberNo() == memberNo.intValue())
                .orElse(false);
    }

    /**
     * Boolean 파싱 헬퍼
     * @param value Object 값
     * @return boolean 값
     */
    private boolean parseBoolean(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
        }
        if (value instanceof Number) {
            return ((Number) value).intValue() != 0;
        }
        return false;
    }
}