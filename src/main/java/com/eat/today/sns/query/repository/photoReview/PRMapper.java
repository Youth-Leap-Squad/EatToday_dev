package com.eat.today.sns.query.repository.photoReview;

import com.eat.today.sns.query.dto.photoReview.PRDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PRMapper {

    // 리뷰 단건 조회 (이미지 포함)
    PRDTO findByReviewNo(@Param("reviewNo") int reviewNo);

    // 게시판별 목록 조회
    List<PRDTO> findByBoardNo(@Param("boardNo") int boardNo);

    // 멤버별 목록 조회
    List<PRDTO> findByMemberNo(@Param("memberNo") int memberNo);

    // 게시판 별 목록 조회
    List<PRDTO> findLatestByBoard(@Param("boardNo") int boardNo);
}
