package com.eat.today.sns.query.service.follow;

import com.eat.today.sns.query.dto.follow.FollowDTO;

import java.util.List;

public interface FollowService {
    List<FollowDTO> getByFollowerNo(int followingNo);  // 나를 팔로우하는 사람
    List<FollowDTO> getByFollowingNo(int followerNo);  // 내가 팔로잉하는 사람

}
