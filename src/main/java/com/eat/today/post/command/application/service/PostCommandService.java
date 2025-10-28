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

    FoodPostResponse updatePost(Integer boardNo, Integer currentMemberNo, UpdateFoodPostRequest req);

    FoodPostResponse approve(Integer boardNo, boolean approved);

    void deletePost(Integer boardNo);

    void cancelPost(Integer boardNo, Integer memberNo);

    void increaseView(Integer boardNo);

    // --- 댓글 ---
    CommentResponse addComment(AddCommentRequest req);

    CommentResponse updateCommentById(Integer commentId, Integer memberNo, String content);

    void deleteCommentById(Integer commentId, Integer memberNo);

    // --- 반응 ---
    ReactionResponse addReaction(Integer boardNo, ReactRequest req);

    ReactionResponse changeReaction(Integer boardNo, ReactRequest req);

    void deleteReaction(Integer boardNo, Integer memberNo);

    // --- 즐겨찾기(폴더형) ---
    void createFolder(Integer memberNo, String folderName);
    void renameFolder(Integer memberNo, Integer folderId, String folderName);
    void deleteFolder(Integer memberNo, Integer folderId);

    void addBookmarkToFolder(Integer memberNo, Integer folderId, Integer boardNo);
    void removeBookmarkFromFolder(Integer memberNo, Integer folderId, Integer boardNo);
    void moveBookmark(Integer memberNo, Integer fromFolderId, Integer toFolderId, Integer boardNo);

    // --- 이미지 업로드(단일) ---
    AlcoholResponse createAlcoholWithImage(CreateAlcoholRequest req,
                                           org.springframework.web.multipart.MultipartFile image);

    AlcoholResponse updateAlcoholWithImage(Integer alcoholNo, UpdateAlcoholRequest req,
                                           org.springframework.web.multipart.MultipartFile image);

    // --- 이미지 업로드(복수) ---
    FoodPostResponse createPostWithImages(CreateFoodPostRequest req,
                                          org.springframework.web.multipart.MultipartFile[] images);

    FoodPostResponse updatePostWithImages(Integer boardNo, Integer currentMemberNo, UpdateFoodPostRequest req,
                                          org.springframework.web.multipart.MultipartFile[] images);
}
