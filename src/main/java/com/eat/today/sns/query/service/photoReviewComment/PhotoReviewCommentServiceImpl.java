package com.eat.today.sns.query.service.photoReviewComment;

import com.eat.today.sns.query.dto.photoReviewComment.PhotoReviewCommentDTO;
import com.eat.today.sns.query.repository.photoReviewComment.PhotoReviewCommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoReviewCommentServiceImpl implements PhotoReviewCommentService {
    private final PhotoReviewCommentMapper mapper;

    @Override
    public List<PhotoReviewCommentDTO> getByReviewNo(int reviewNo) {
        return mapper.getByReviewNo(reviewNo);
    }
}

