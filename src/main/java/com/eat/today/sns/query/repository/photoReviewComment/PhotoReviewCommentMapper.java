package com.eat.today.sns.query.repository.photoReviewComment;

import com.eat.today.sns.query.dto.photoReviewComment.PhotoReviewCommentDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PhotoReviewCommentMapper {
    List<PhotoReviewCommentDTO> getByReviewNo(int reviewNo);
}
