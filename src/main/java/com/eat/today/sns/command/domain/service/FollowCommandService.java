package com.eat.today.sns.command.domain.service;

import com.eat.today.sns.command.application.entity.follow.FollowEntity;
import com.eat.today.sns.command.domain.repository.follow.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FollowCommandService {
    private final FollowRepository repo;
    private final FollowRepository followRepository;

    @Transactional
    public void follow(int followerNo, int followingNo) {
        if (Objects.equals(followerNo, followingNo)) {
            throw new IllegalArgumentException("자기 자신은 팔로우할 수 없습니다.");
        }
        if (repo.existsByFollowerNoAndFollowingNo(followerNo, followingNo)) {
            return; // 이미 팔로우 상태면 무시
        }
        FollowEntity f = new FollowEntity();
        f.setFollowerNo(followerNo);
        f.setFollowingNo(followingNo);
        repo.save(f);
    }

    /** 나를 팔로우한 사람 삭제: (follower -> me) */
    @Transactional
    public int removeFollower(int me, int follower) {
        long affected = repo.deleteByFollowerNoAndFollowingNo(follower, me); // ← 순서 중요!
        return (int) affected;
    }

    /** 내가 팔로우한 사람 삭제: (follower -> me) */
    @Transactional
    public int removeFollowing(int me, int following) {
        long affected = repo.deleteByFollowerNoAndFollowingNo(me, following);
        return (int) affected;
    }
}