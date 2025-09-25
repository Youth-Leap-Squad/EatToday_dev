package com.eat.today.post.query.mapper;

import com.eat.today.post.query.dto.*;
import com.eat.today.post.query.dto.AlcoholDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper {

    // 기존: 전체 술 / 전체 안주
    List<AlcoholDTO> selectAlcoholList(@Param("offset") int offset, @Param("limit") int limit);

    // 승인된 게시글 목록(반응 4종 포함)
    List<FoodDTO> selectFoodList(@Param("offset") int offset, @Param("limit") int limit);

    // alcohol (설명/사진만)
    List<AlcoholSimpleDTO> selectAlcoholExplainAndPicture();

    // bookmark: 특정 회원 즐겨찾기 목록
    List<BookmarkDTO> selectBookmarksByMember(@Param("memberNo") int memberNo);

    // food_post_likes: 특정 게시글 반응 통계
    List<FoodPostLikesStatDTO> selectFoodPostLikeStats(@Param("boardNo") int boardNo);

    // food_comment: 특정 게시글 댓글 목록
    List<FoodCommentDTO> selectFoodComments(@Param("boardNo") int boardNo);

    // 내가 쓴 댓글(회원별, 최신순)
    List<MyCommentDTO> selectMyComments(@Param("memberNo") int memberNo);

    // 인기 게시글 TOP N
    List<PopularFoodDTO> selectPopularFoods(@Param("limit") int limit);
}
