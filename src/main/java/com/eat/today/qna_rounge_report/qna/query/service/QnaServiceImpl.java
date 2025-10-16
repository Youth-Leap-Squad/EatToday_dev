package com.eat.today.qna_rounge_report.qna.query.service;

import com.eat.today.qna_rounge_report.qna.query.dto.QnaCommentDTO;
import com.eat.today.qna_rounge_report.qna.query.dto.QnaPageResponse;
import com.eat.today.qna_rounge_report.qna.query.dto.QnaPostDTO;
import com.eat.today.qna_rounge_report.qna.query.repository.QnaMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QnaServiceImpl implements QnaService {

    private final QnaMapper mapper;

    public QnaServiceImpl(QnaMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public QnaPageResponse<QnaPostDTO> getPostsOrderByDateDescPaged(int page, int size) {
        int offset = page * size;
        List<QnaPostDTO> list = mapper.selectPostsOrderByDateDescPaged(offset, size);
        long total = mapper.countAllPosts();
        return new QnaPageResponse<>(list, page, size, total);
    }

    @Override
    public QnaPageResponse<QnaPostDTO> searchPostsPaged(String keyword, int page, int size) {
        int offset = page * size;
        List<QnaPostDTO> list = mapper.searchPostsPaged(keyword, offset, size);
        long total = mapper.countPostsByKeyword(keyword);
        return new QnaPageResponse<>(list, page, size, total);
    }

    @Override
    public QnaPageResponse<QnaPostDTO> getPostsByMemberNoPaged(int memberNo, int page, int size) {
        int offset = page * size;
        List<QnaPostDTO> list = mapper.selectPostsByMemberNoPaged(memberNo, offset, size);
        long total = mapper.countPostsByMemberNo(memberNo);
        return new QnaPageResponse<>(list, page, size, total);
    }

    @Override
    public QnaPostDTO getPostById(int qnaPostNo) {
        return mapper.selectPostById(qnaPostNo);
    }

    @Override
    public QnaPageResponse<QnaCommentDTO> getCommentsByPostIdPaged(int qnaPostNo, int page, int size) {
        int offset = page * size;
        List<QnaCommentDTO> list = mapper.selectCommentsByPostIdPaged(qnaPostNo, offset, size);
        long total = mapper.countCommentsByPostId(qnaPostNo);
        return new QnaPageResponse<>(list, page, size, total);
    }
}
