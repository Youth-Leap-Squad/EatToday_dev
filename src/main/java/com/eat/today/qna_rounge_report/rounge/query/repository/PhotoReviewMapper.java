package com.eat.today.qna_rounge_report.rounge.query.repository;

import com.eat.today.qna_rounge_report.rounge.query.dto.PhotoReviewDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PhotoReviewMapper {

    // 전체 리뷰 조회(날짜 별 내림차순)
    List<PhotoReviewDTO> selectAllOrderByDateDesc();

    // 전체 리뷰 조회(좋아요 수 내림차순)
    List<PhotoReviewDTO> selectAllOrderByLikeDesc();
}
