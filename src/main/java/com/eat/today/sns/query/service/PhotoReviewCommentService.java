package com.eat.today.sns.query.service;

import com.eat.today.sns.query.dto.PhotoReviewCommentDTO;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PhotoReviewCommentService {
    List<PhotoReviewCommentDTO> getByReviewNo(int reviewNo);
}
