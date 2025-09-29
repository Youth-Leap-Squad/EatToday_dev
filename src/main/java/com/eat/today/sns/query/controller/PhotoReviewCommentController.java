package com.eat.today.sns.query.controller;

import com.eat.today.sns.query.dto.PhotoReviewCommentDTO;
import com.eat.today.sns.query.repository.PhotoReviewCommentMapper;
import com.eat.today.sns.query.service.PhotoReviewCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/prc")
public class PhotoReviewCommentController {

    private final PhotoReviewCommentService service;

    // 생성자 주입 (권장)
    public PhotoReviewCommentController(PhotoReviewCommentService service) {
        this.service = service;
    }

    @GetMapping("/{reviewNo}")
    public ResponseEntity<List<PhotoReviewCommentDTO>> list(@PathVariable int reviewNo) {
        return ResponseEntity.ok(service.getByReviewNo(reviewNo));
    }
}

