package com.eat.today.sns.command.domain.repository.PhotoReviewComment;

import com.eat.today.sns.command.application.entity.photoReviewComment.PhotoReviewCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PhotoReviewCommentRepository extends JpaRepository<PhotoReviewCommentEntity, Integer> {

    List<PhotoReviewCommentEntity> findByReviewNoAndPrcDeletedFalse(int reviewNo);
    Optional<PhotoReviewCommentEntity> findByPrcNoAndMemberNoAndPrcDeletedFalse(int prcNo, int memberNo);
}
