package com.eat.today.sns.query.controller.follow;

import com.eat.today.sns.query.dto.follow.FollowDTO;
import com.eat.today.sns.query.service.follow.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    // GET /follow/2  -> 나를 팔로우한 사람들
    @GetMapping("/{following_no}")
    public List<FollowDTO> getByFollowerNoShort(@PathVariable("following_no") int followingNo) {
        return followService.getByFollowerNo(followingNo);
    }

    @GetMapping("/follower/{following_no}")
    public List<FollowDTO> getByFollowerNo(@PathVariable("following_no") int followingNo) {
        return followService.getByFollowerNo(followingNo);
    }

    // GET /follow/following/2 -> 내가 팔로잉한 사람
    @GetMapping("/following/{follower_no}")
    public List<FollowDTO> getByFollowingNo(@PathVariable("follower_no") int followerNo) {
        return followService.getByFollowingNo(followerNo);
    }
}
