package com.eat.today.sns.query.controller.photoReviewComment;

import com.eat.today.sns.query.dto.photoReviewComment.PhotoReviewCommentDTO;
import com.eat.today.sns.query.service.photoReviewComment.PhotoReviewCommentService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public PhotoReviewCommentController(PhotoReviewCommentService service) {
        this.service = service;
    }

    @GetMapping("/{reviewNo}")
    public ResponseEntity<List<PhotoReviewCommentDTO>> list(@PathVariable int reviewNo) {
        return ResponseEntity.ok(service.getByReviewNo(reviewNo));
    }
}

