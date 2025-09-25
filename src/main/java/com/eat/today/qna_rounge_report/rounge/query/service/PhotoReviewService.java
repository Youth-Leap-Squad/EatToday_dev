package com.eat.today.qna_rounge_report.rounge.query.service;

import com.eat.today.qna_rounge_report.rounge.query.dto.PhotoReviewDTO;

import java.util.List;

public interface PhotoReviewService {

    /** 날짜 내림차순 전체 조회 */
    List<PhotoReviewDTO> getAllByDateDesc();

    /** 좋아요 수 내림차순 전체 조회 */
    List<PhotoReviewDTO> getAllByLikeDesc();

}
