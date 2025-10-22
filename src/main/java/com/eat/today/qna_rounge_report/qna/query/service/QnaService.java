package com.eat.today.qna_rounge_report.qna.query.service;

import com.eat.today.qna_rounge_report.qna.query.dto.QnaCommentDTO;
import com.eat.today.qna_rounge_report.qna.query.dto.QnaPageResponse;
import com.eat.today.qna_rounge_report.qna.query.dto.QnaPostDTO;

public interface QnaService {
    QnaPageResponse<QnaPostDTO> getPostsOrderByDateDescPaged(int page, int size);
    QnaPageResponse<QnaPostDTO> searchPostsPaged(String keyword, int page, int size);
    QnaPageResponse<QnaPostDTO> getPostsByMemberNoPaged(int memberNo, int page, int size);
    QnaPostDTO getPostById(int qnaPostNo);
    QnaPageResponse<QnaCommentDTO> getCommentsByPostIdPaged(int qnaPostNo, int page, int size);
}