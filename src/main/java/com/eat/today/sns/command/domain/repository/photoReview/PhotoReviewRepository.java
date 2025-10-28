package com.eat.today.sns.command.domain.repository.photoReview;

import com.eat.today.sns.command.application.entity.photoReview.PhotoReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PhotoReviewRepository extends JpaRepository<PhotoReviewEntity, Integer> {
}
