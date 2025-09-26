package com.eat.today.sns.query.repository;

import com.eat.today.sns.query.dto.PhotoReviewCommentDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface PhotoReviewCommentMapper {
    List<PhotoReviewCommentDTO> getByReviewNo(int reviewNo);
}
