package com.eat.today.sns.command.domain.service;

import com.eat.today.sns.command.application.entity.photoReviewComment.PhotoReviewCommentEntity;
import com.eat.today.sns.command.domain.repository.PhotoReviewComment.PhotoReviewCommentRepository;
import com.eat.today.sns.query.dto.photoReviewComment.PrcDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PhotoReviewCommentCommandService {

    private final PhotoReviewCommentRepository repo;
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public PhotoReviewCommentCommandService(PhotoReviewCommentRepository repo) {
        this.repo = repo;
    }

    // 댓글 추가
    @Transactional
    public PrcDTO.CreateResponse create(int memberNo, int reviewNo, PrcDTO.CreateRequest req) {
        PhotoReviewCommentEntity e = new PhotoReviewCommentEntity();
        e.setMemberNo(memberNo);
        e.setReviewNo(reviewNo);
        e.setPrcDetail(req.getDetail());
        e.setPrcAt(LocalDateTime.now().format(ISO));
        e.setPrcDeleted(false);
        var saved = repo.save(e);
        return new PrcDTO.CreateResponse(saved.getPrcNo());
    }

    // 댓글 수정
    @Transactional
    public int edit(int memberNo, int prcNo, PrcDTO.UpdateRequest req) {
        return repo.findByPrcNoAndMemberNoAndPrcDeletedFalse(prcNo, memberNo)
                .map(entity -> {
                    entity.setPrcDetail(req.getDetail());
                    entity.setPrcAt(LocalDateTime.now().format(ISO));
                    repo.save(entity);
                    return 1;
                })
                .orElse(0);
    }

    /** 하드 삭제: 존재하고 본인 글이면 1, 아니면 0 */
    @Transactional
    public int deleteHard(int memberNo, int prcNo) {
        return repo.findByPrcNoAndMemberNoAndPrcDeletedFalse(prcNo, memberNo)
                .map(e -> {
                    repo.delete(e);
                    return 1;
                })
                .orElse(0);
    }

    /** 소프트 삭제 예시 */
    @Transactional
    public int deleteSoft(int memberNo, int prcNo) {
        return repo.findByPrcNoAndMemberNoAndPrcDeletedFalse(prcNo, memberNo)
                .map(e -> { e.setPrcDeleted(true); return 1; })
                .orElse(0);
    }
}
