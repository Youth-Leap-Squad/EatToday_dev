package com.eat.today.post.query.service;

import com.eat.today.post.query.dto.*;
import com.eat.today.post.query.dto.AlcoholDTO;

import java.util.List;

public interface PostService {

    // 전체 조회
    List<AlcoholDTO> getAlcohols(int page, int size);

    // 승인된 게시글 목록(반응 4종 포함)
    List<FoodDTO> getFoods(int page, int size);

    // alcohol: 설명/사진만
    List<AlcoholSimpleDTO> getAlcoholExplainAndPicture();

    // bookmark: 특정 회원 즐겨찾기
    List<BookmarkDTO> getBookmarksByMember(int memberNo);

    // food_post_likes: 특정 게시글 반응 통계
    List<FoodPostLikesStatDTO> getFoodPostLikeStats(int boardNo);

    // food_comment: 특정 게시글 댓글
    List<FoodCommentDTO> getFoodComments(int boardNo);

    // 내가 쓴 댓글(회원별, 최신순)
    List<MyCommentDTO> getMyComments(int memberNo);

    // 인기 게시글 TOP N
    List<PopularFoodDTO> getPopularFoods(int limit);
}
