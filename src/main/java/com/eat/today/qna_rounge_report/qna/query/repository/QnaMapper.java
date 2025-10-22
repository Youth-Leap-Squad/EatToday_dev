package com.eat.today.qna_rounge_report.qna.query.repository;

import com.eat.today.qna_rounge_report.qna.query.dto.QnaPostDTO;
import com.eat.today.qna_rounge_report.qna.query.dto.QnaCommentDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface QnaMapper {

    // 게시글
    List<QnaPostDTO> selectPostsOrderByDateDescPaged(@Param("offset") int offset, @Param("size") int size);
    long countAllPosts();

    List<QnaPostDTO> searchPostsPaged(@Param("keyword") String keyword,
                                      @Param("offset") int offset,
                                      @Param("size") int size);
    long countPostsByKeyword(@Param("keyword") String keyword);

    List<QnaPostDTO> selectPostsByMemberNoPaged(@Param("memberNo") int memberNo,
                                                @Param("offset") int offset,
                                                @Param("size") int size);
    long countPostsByMemberNo(@Param("memberNo") int memberNo);

    QnaPostDTO selectPostById(@Param("qnaPostNo") int qnaPostNo);

    // 댓글
    List<QnaCommentDTO> selectCommentsByPostIdPaged(@Param("qnaPostNo") int qnaPostNo,
                                                    @Param("offset") int offset,
                                                    @Param("size") int size);
    long countCommentsByPostId(@Param("qnaPostNo") int qnaPostNo);
}