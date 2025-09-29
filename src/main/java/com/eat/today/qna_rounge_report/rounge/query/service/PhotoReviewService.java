package com.eat.today.qna_rounge_report.rounge.query.service;

import com.eat.today.qna_rounge_report.rounge.query.dto.PhotoReviewDTO;

import java.util.List;

public interface PhotoReviewService {

    /** 날짜 내림차순 전체 조회 */
    List<PhotoReviewDTO> getAllByDateDesc();

    /** 좋아요 수 내림차순 전체 조회 */
    List<PhotoReviewDTO> getAllByLikeDesc();

    /** 검색 조회(제목, 내용) */
    List<PhotoReviewDTO> search(String keyword);

    /** 검색을 통한 리뷰 조회 */
    List<PhotoReviewDTO> getByAlcoholNo(int alcoholNo);
}
