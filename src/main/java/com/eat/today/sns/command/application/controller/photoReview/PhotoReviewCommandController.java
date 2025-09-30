package com.eat.today.sns.command.application.controller.photoReview;


import com.eat.today.sns.command.application.dto.photoReviewDTO.CreateRequest;
import com.eat.today.sns.command.application.dto.photoReviewDTO.UpdateRequest;
import com.eat.today.sns.command.domain.service.PhotoReviewCommandService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/command/photo-reviews")
public class PhotoReviewCommandController {

    private final PhotoReviewCommandService service;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> create(@Valid @RequestBody CreateRequest req) {
        int reviewNo = service.create(req);
        return ResponseEntity.ok(Map.of("reviewNo", reviewNo));
    }

    @PatchMapping(path = "/{reviewNo}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> update(@PathVariable int reviewNo,
                                    @RequestBody UpdateRequest req) {
        try {
            int affected = service.edit(reviewNo, req);
            return ResponseEntity.ok(Map.of("updated", affected));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{reviewNo}")
    public ResponseEntity<?> delete(@PathVariable int reviewNo) {
        int affected = service.delete(reviewNo);
        return affected > 0 ? ResponseEntity.ok(Map.of("deleted", affected))
                : ResponseEntity.notFound().build();
    }
}
