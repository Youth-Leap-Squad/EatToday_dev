package com.eat.today.sns.command.domain.service;

import com.eat.today.sns.command.application.dto.photoReviewDTO.CreateRequest;
import com.eat.today.sns.command.application.dto.photoReviewDTO.UpdateRequest;
import com.eat.today.sns.command.application.entity.photoReview.PhotoReviewEntity;
import com.eat.today.sns.command.domain.repository.photoReview.PhotoReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PhotoReviewCommandService {

    private final PhotoReviewRepository repository;

    @Transactional
    public int create(CreateRequest req) {
        PhotoReviewEntity e = new PhotoReviewEntity();
        e.setBoardNo(req.getBoardNo());
        e.setMemberNo(req.getMemberNo());
        e.setReviewTitle(req.getReviewTitle());
        e.setReviewDate(req.getReviewDate());
        e.setReviewContent(req.getReviewContent());
        e.setReviewLike(req.getReviewLike());
        return repository.save(e).getReviewNo();
    }

    @Transactional
    public int edit(int reviewNo, UpdateRequest req) {
        PhotoReviewEntity e = repository.findById(reviewNo)
                .orElseThrow(() -> new EntityNotFoundException("review_no=" + reviewNo));

        if (req.getBoardNo() != null) e.setBoardNo(req.getBoardNo());
        if (req.getMemberNo() != null) e.setMemberNo(req.getMemberNo());
        if (req.getReviewTitle() != null) e.setReviewTitle(req.getReviewTitle());
        if (req.getReviewDate() != null) e.setReviewDate(req.getReviewDate());
        if (req.getReviewContent() != null) e.setReviewContent(req.getReviewContent());
        if (req.getReviewLike() != null) e.setReviewLike(req.getReviewLike());

        repository.save(e);
        return 1;
    }

    @Transactional
    public int delete(int reviewNo) {
        if (!repository.existsById(reviewNo)) return 0;
        repository.deleteById(reviewNo);
        return 1;
    }
}