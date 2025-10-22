package com.eat.today.sns.command.application.controller.photoReview;


import com.eat.today.sns.command.application.dto.photoReviewDTO.CreateRequest;
import com.eat.today.sns.command.application.dto.photoReviewDTO.UpdateRequest;
import com.eat.today.sns.command.domain.service.PhotoReviewCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/command/photo-reviews")
public class PhotoReviewCommandController {

    private final PhotoReviewCommandService service;

    /* CREATE: 본문 + 이미지들 동시 등록 */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(
            @Valid @RequestPart("review") CreateRequest review,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) throws IOException {
        int reviewNo = service.create(review, files == null ? List.of() : files);
        return ResponseEntity.ok(Map.of("reviewNo", reviewNo));
    }

    /* UPDATE: 부분수정 + 이미지 추가/삭제 */
    @PatchMapping(path = "/{reviewNo}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> edit(
            @PathVariable int reviewNo,
            @RequestPart(value = "review", required = false) UpdateRequest reviewPatch,
            @RequestPart(value = "addFiles", required = false) List<MultipartFile> addFiles,
            @RequestPart(value = "deleteFileNos", required = false) List<Integer> deleteFileNos
    ) throws IOException {
        int updated = service.editWithFiles(
                reviewNo,
                reviewPatch,
                addFiles == null ? List.of() : addFiles,
                deleteFileNos == null ? List.of() : deleteFileNos
        );
        return updated > 0 ? ResponseEntity.ok(Map.of("updated", updated))
                : ResponseEntity.notFound().build();
    }

    /* DELETE: 리뷰 + 연관 파일 전체 삭제 */
    @DeleteMapping("/{reviewNo}")
    public ResponseEntity<?> delete(@PathVariable int reviewNo) {
        int affected = service.delete(reviewNo);
        return affected > 0 ? ResponseEntity.ok(Map.of("deleted", affected))
                : ResponseEntity.notFound().build();
    }
}
