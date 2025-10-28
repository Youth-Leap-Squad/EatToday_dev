package com.eat.today.sns.command.domain.service;

import com.eat.today.sns.command.application.entity.photoReviewComment.PhotoReviewCommentEntity;
import com.eat.today.sns.command.domain.repository.PhotoReviewComment.PhotoReviewCommentRepository;
import com.eat.today.sns.query.dto.photoReviewComment.PrcDTO;
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

    /** 댓글 추가 */
    @Transactional
    public PrcDTO.CreateResponse create(int memberNo, int reviewNo, PrcDTO.CreateRequest req) {
        PhotoReviewCommentEntity e = new PhotoReviewCommentEntity();
        e.setMemberNo(memberNo);
        e.setReviewNo(reviewNo);

        // prcDetail 또는 content 둘 다 허용
        String detail = (req.getPrcDetail() != null && !req.getPrcDetail().isBlank())
                ? req.getPrcDetail()
                : null;

        if (detail == null || detail.isBlank()) {
            throw new IllegalArgumentException("댓글 내용(prcDetail)이 비어 있습니다.");
        }

        e.setPrcDetail(detail);
        e.setPrcAt(LocalDateTime.now().format(ISO));
        e.setPrcDeleted(false);

        var saved = repo.save(e);
        return new PrcDTO.CreateResponse(saved.getPrcNo());
    }

    /** 댓글 수정 */
    @Transactional
    public int edit(int memberNo, int prcNo, PrcDTO.UpdateRequest req) {
        var opt = repo.findByPrcNoAndMemberNoAndPrcDeletedFalse(prcNo, memberNo);
        if (opt.isEmpty()) return 0;
        var e = opt.get();

        String newDetail = req.getPrcDetail();
        if (newDetail == null || newDetail.isBlank()) {
            throw new IllegalArgumentException("댓글 내용이 비어있습니다.");
        }

        e.setPrcDetail(newDetail);
        e.setPrcAt(LocalDateTime.now().format(ISO));
        repo.save(e);
        return 1;
    }

    /** 댓글 하드 삭제 */
    @Transactional
    public int deleteHard(int memberNo, int prcNo) {
        return repo.findByPrcNoAndMemberNoAndPrcDeletedFalse(prcNo, memberNo)
                .map(e -> {
                    repo.delete(e);
                    return 1;
                })
                .orElse(0);
    }

    /** 댓글 소프트 삭제 */
    @Transactional
    public int deleteSoft(int memberNo, int prcNo) {
        return repo.findByPrcNoAndMemberNoAndPrcDeletedFalse(prcNo, memberNo)
                .map(e -> {
                    e.setPrcDeleted(true);
                    e.setPrcAt(LocalDateTime.now().format(ISO));
                    return 1;
                })
                .orElse(0);
    }
}