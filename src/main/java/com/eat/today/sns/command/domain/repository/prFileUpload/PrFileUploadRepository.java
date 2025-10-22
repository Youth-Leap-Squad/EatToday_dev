package com.eat.today.sns.command.domain.repository.prFileUpload;

import com.eat.today.sns.command.application.entity.prFileUpload.PrFileUploadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrFileUploadRepository extends JpaRepository<PrFileUploadEntity, Integer> {
    List<PrFileUploadEntity> findByReview_ReviewNo(int reviewNo);
}