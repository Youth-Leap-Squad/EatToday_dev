package com.eat.today.sns.command.domain.repository.photoReview;

import com.eat.today.sns.command.application.entity.photoReview.PhotoReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoReviewRepository extends JpaRepository<PhotoReviewEntity, Integer> {
}
