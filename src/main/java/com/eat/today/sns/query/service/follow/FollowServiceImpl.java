package com.eat.today.sns.query.service.follow;

import com.eat.today.sns.query.dto.follow.FollowDTO;
import com.eat.today.sns.query.repository.follow.FollowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {
    private final FollowMapper followMapper;

    @Override
    public List<FollowDTO> getByFollowingNo(int followerNo) {
        return followMapper.selectByFollowingNo(followerNo);
    }

    @Override
    public List<FollowDTO> getByFollowerNo(int followingNo) {
        return followMapper.selectByFollowerNo(followingNo);
    }
}
