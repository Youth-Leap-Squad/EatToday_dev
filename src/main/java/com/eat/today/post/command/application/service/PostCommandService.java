package com.eat.today.post.command.application.service;

import com.eat.today.post.command.application.dto.*;
import java.util.List;

public interface PostCommandService {

    // --- 술 종류 ---
    AlcoholResponse createAlcohol(CreateAlcoholRequest req);
    AlcoholResponse updateAlcohol(Integer alcoholNo, UpdateAlcoholRequest req);
    void deleteAlcohol(Integer alcoholNo);

    // --- 게시글(안주) ---
    FoodPostResponse createPost(CreateFoodPostRequest req);
    FoodPostResponse updatePost(Integer boardNo, UpdateFoodPostRequest req);
    FoodPostResponse approve(Integer boardNo, boolean approved);
    void deletePost(Integer boardNo);
    void cancelPost(Integer boardNo, Integer memberNo);

    // --- 댓글 ---
    CommentResponse addComment(AddCommentRequest req);
    CommentResponse updateCommentById(Integer commentId, Integer memberNo, String content);
    void deleteCommentById(Integer commentId, Integer memberNo);

    // --- 반응 ---
    ReactionResponse addReaction(Integer boardNo, ReactRequest req);
    ReactionResponse changeReaction(Integer boardNo, ReactRequest req);
    void deleteReaction(Integer boardNo, Integer memberNo);

    // --- 즐겨찾기 ---
    List<BookmarkResponse> addBookmark(AddBookmarkRequest req);
    List<BookmarkResponse> removeBookmark(Integer memberNo, Integer boardNo);
}
