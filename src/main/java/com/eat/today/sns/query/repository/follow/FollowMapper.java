package com.eat.today.sns.query.repository.follow;

import com.eat.today.sns.query.dto.follow.FollowDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FollowMapper {
        List<FollowDTO> selectByFollowerNo(@Param("followingNo") int followingNo);
        List<FollowDTO> selectByFollowingNo(@Param("followerNo") int followerNo);
}
