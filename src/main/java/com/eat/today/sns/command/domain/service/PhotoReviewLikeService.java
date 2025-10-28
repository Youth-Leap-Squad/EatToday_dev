// src/main/java/com/eat/today/sns/command/domain/service/PhotoReviewLikeService.java
package com.eat.today.sns.command.domain.service;

import com.eat.today.sns.command.application.entity.photoReview.PhotoReviewEntity;
import com.eat.today.sns.command.domain.repository.photoReview.PhotoReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class PhotoReviewLikeService {

    private final PhotoReviewRepository reviewRepo;

    /** 서버 메모리로 유저별 좋아요 여부 유지: key = reviewNo + ":" + memberNo */
    private final Map<String, Boolean> likedByUser = new ConcurrentHashMap<>();

    private static String key(int reviewNo, Integer memberNo){
        return reviewNo + ":" + (memberNo == null ? 0 : memberNo);
    }

    @Transactional
    public Map<String, Object> toggleLike(int reviewNo, Integer memberNo) {
        PhotoReviewEntity review = reviewRepo.findById(reviewNo).orElseThrow();

        String k = key(reviewNo, memberNo);
        boolean currentlyLiked = likedByUser.getOrDefault(k, false);

        int cnt = Optional.ofNullable(review.getReviewLike()).orElse(0);
        if (currentlyLiked) {
            // 취소
            cnt = Math.max(0, cnt - 1);
            likedByUser.put(k, false);
        } else {
            // 좋아요
            cnt = cnt + 1;
            likedByUser.put(k, true);
        }

        review.setReviewLike(cnt);
        reviewRepo.save(review);

        return Map.of(
                "reviewNo", reviewNo,
                "likeCount", cnt,
                "liked", likedByUser.get(k)
        );
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getStatus(int reviewNo, Integer memberNo) {
        PhotoReviewEntity review = reviewRepo.findById(reviewNo).orElseThrow();
        int cnt = Optional.ofNullable(review.getReviewLike()).orElse(0);
        boolean liked = likedByUser.getOrDefault(key(reviewNo, memberNo), false);
        return Map.of(
                "reviewNo", reviewNo,
                "likeCount", cnt,
                "liked", liked
        );
    }
}