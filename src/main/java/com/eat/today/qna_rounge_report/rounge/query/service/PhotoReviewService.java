package com.eat.today.qna_rounge_report.rounge.query.service;

import com.eat.today.qna_rounge_report.rounge.query.dto.PhotoReviewDTO;
import com.eat.today.qna_rounge_report.rounge.query.dto.PhotoReviewPageResponse;

import java.util.List;


public interface PhotoReviewService {

    /** 날짜 내림차순 전체 조회 */
    PhotoReviewPageResponse getAllByDateDescPaged(int page, int size);

    /** 좋아요 수 내림차순 전체 조회 */
    PhotoReviewPageResponse getAllByLikeDescPaged(int page, int size);

    /** 검색 조회(제목, 내용) */
    PhotoReviewPageResponse searchPaged(String keyword, int page, int size);

    /** 검색을 통한 리뷰 조회 */
    PhotoReviewPageResponse getByAlcoholNoPaged(int alcoholNo, int page, int size);

    /** 특정 사용자의 리뷰 조회 */
    PhotoReviewPageResponse getByMemberNoPaged(int memberNo, int page, int size);

    PhotoReviewPageResponse getByAlcoholNoLikeDescPaged(int alcoholNo, int page, int size);
}
