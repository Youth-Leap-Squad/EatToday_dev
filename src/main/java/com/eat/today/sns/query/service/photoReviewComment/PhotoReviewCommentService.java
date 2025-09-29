package com.eat.today.sns.query.service.photoReviewComment;

import com.eat.today.sns.query.dto.photoReviewComment.PhotoReviewCommentDTO;

import java.util.List;

public interface PhotoReviewCommentService {
    List<PhotoReviewCommentDTO> getByReviewNo(int reviewNo);
}
