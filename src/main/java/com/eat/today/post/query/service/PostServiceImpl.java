package com.eat.today.post.query.service;

import com.eat.today.post.query.dto.*;
import com.eat.today.post.query.dto.AlcoholDTO;
import com.eat.today.post.query.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;

    // ---------- 공통: 페이징 헬퍼 ----------

    private int safeLimit(int size)  {
        return Math.max(1, size);
    }
    private int safeOffset(int page, int size) {
        return Math.max(0, page) * safeLimit(size);
    }

    private int offset(int page, int size) {
        return Math.max(page, 0) * Math.max(size, 1);
    }

    // ---------- 전체 조회 ----------

    @Override
    public List<AlcoholDTO> getAlcohols(int page, int size) {
        int limit  = safeLimit(size);
        int offset = safeOffset(page, size);
        return postMapper.selectAlcoholList(offset, limit);
    }

    // ---------- 승인된 게시글 목록(반응 포함) ----------


    @Override
    public List<FoodDTO> getApprovedFoods(int page, int size) {
        return postMapper.selectApprovedFoodList(offset(page, size), size);
    }

    @Override
    public List<FoodDTO> getFoodsSortedByViews(int page, int size) {
        return postMapper.selectFoodsOrderByViews(offset(page, size), size);
    }

    @Override
    public List<FoodDTO> getFoodsSortedByComments(int page, int size) {
        return postMapper.selectFoodsOrderByComments(offset(page, size), size);
    }

    @Override
    public List<FoodDTO> getFoodsSortedByTotalLikes(int page, int size) {
        return postMapper.selectFoodsOrderByTotalLikes(offset(page, size), size);
    }

    @Override
    public List<FoodDTO> getUnapprovedFoods(int page, int size) {
        int limit  = safeLimit(size);
        int offset = safeOffset(page, size);
        return postMapper.selectUnapprovedFoodList(offset, limit);
    }

    @Override
    public List<FoodDTO> getAllFoods(int page, int size) {
        int limit  = safeLimit(size);
        int offset = safeOffset(page, size);
        return postMapper.selectAllFoodList(offset, limit);
    }

    @Override
    public List<MyFoodDTO> getMyFoods(int memberNo, int page, int size) {
        int limit  = safeLimit(size);
        int offset = safeOffset(page, size);
        return postMapper.selectMyFoods(memberNo, offset, limit);
    }

    // ---------- 특정 술에 대한 승인된 안주 ----------

    @Override
    public List<FoodDTO> getApprovedFoodsByAlcohol(int alcoholNo, int page, int size) {
        int limit  = safeLimit(size);
        int offset = safeOffset(page, size);
        return postMapper.selectApprovedFoodsByAlcohol(alcoholNo, offset, limit);
    }


    // ---------- alcohol: 설명/사진만 ----------

    @Override
    public List<AlcoholSimpleDTO> getAlcoholExplainAndPicture() {
        return postMapper.selectAlcoholExplainAndPicture();
    }

    // ---------- food_post_likes ----------

    @Override
    public List<FoodPostLikesDTO> getFoodPostLikesFromPost(int boardNo) {
        return postMapper.selectFoodLikesFromPost(boardNo);
    }

    // ---------- food_comment ----------

    @Override
    public List<FoodCommentDTO> getFoodComments(int boardNo) {
        return postMapper.selectFoodComments(boardNo);
    }

    // ---------- 내가 쓴 댓글 ----------

    @Override
    public List<MyCommentDTO> getMyComments(int memberNo) {
        return postMapper.selectMyComments(memberNo);
    }

    // ---------- bookmark ----------

    @Override
    public List<BookmarkDTO> getBookmarksByMember(int memberNo) {
        return postMapper.selectBookmarksByMember(memberNo);
    }

    // ---------- 인기 게시글 TOP N ----------

    @Override
    public List<PopularFoodDTO> getPopularFoods(int limit) {
        return postMapper.selectPopularFoods(Math.max(1, limit));
    }
}
