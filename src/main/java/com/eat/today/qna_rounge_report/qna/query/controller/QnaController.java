package com.eat.today.qna_rounge_report.qna.query.controller;

import com.eat.today.qna_rounge_report.qna.query.dto.QnaCommentDTO;
import com.eat.today.qna_rounge_report.qna.query.dto.QnaPageResponse;
import com.eat.today.qna_rounge_report.qna.query.dto.QnaPostDTO;
import com.eat.today.qna_rounge_report.qna.query.service.QnaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qna")
public class QnaController {

    private final QnaService service;

    public QnaController(QnaService service) {
        this.service = service;
    }

    /** 1. 게시글: 최신순 목록 (기본 size=10) */
    @GetMapping("/posts/date")
    public QnaPageResponse<QnaPostDTO> getPostsByDateDesc(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.getPostsOrderByDateDescPaged(page, size);
    }

    /** 2. 게시글: 키워드 검색(제목/내용 등) 최신순 */
    @GetMapping("/posts/search")
    public QnaPageResponse<QnaPostDTO> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.searchPostsPaged(keyword, page, size);
    }

    /** 3. 게시글: 내 글 최신순 (memberNo 직접 전달) */
    @GetMapping("/posts/member")
    public QnaPageResponse<QnaPostDTO> getPostsByMemberNo(
            @RequestParam int memberNo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.getPostsByMemberNoPaged(memberNo, page, size);
    }

    /** 4. 게시글: 단건 조회 */
    @GetMapping("/posts/{qnaPostNo}")
    public QnaPostDTO getPostById(@PathVariable int qnaPostNo) {
        return service.getPostById(qnaPostNo);
    }

    /** 5. 댓글: 특정 게시글의 댓글 최신순 */
    @GetMapping("/comments")
    public QnaPageResponse<QnaCommentDTO> getCommentsByPostId(
            @RequestParam int qnaPostNo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.getCommentsByPostIdPaged(qnaPostNo, page, size);
    }
}