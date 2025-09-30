package com.eat.today.sns.command.application.controller.follow;

import com.eat.today.sns.command.application.entity.follow.FollowEntity;
import com.eat.today.sns.command.domain.repository.follow.FollowRepository;
import com.eat.today.sns.command.domain.service.FollowCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/command/follow")
public class FollowCommandController {

    private final FollowRepository followRepository;
    private final FollowCommandService followCommandService;

    @Autowired
    public FollowCommandController(FollowRepository followRepository, FollowCommandService followCommandService) {
        this.followRepository = followRepository;
        this.followCommandService = followCommandService;
    }

    // 팔로우 생성
    @PostMapping
    public ResponseEntity<FollowEntity> createFollow(@RequestBody FollowEntity follow) {
        FollowEntity saved = followRepository.save(follow);
        return ResponseEntity.ok(saved);
    }

    // 나를 팔로우한 사람 삭제
    @DeleteMapping("/command/follow/remove-follower")
    public ResponseEntity<Integer> removeFollower(
            @RequestParam("me") int me,
            @RequestParam("follower") int follower) {
        int affected = followCommandService.removeFollower(me, follower);
        return ResponseEntity.ok(affected);
    }

    // 내가 팔로우한 사람 삭제
    @DeleteMapping("/remove-following")
    public ResponseEntity<?> removeFollowing(@RequestParam int me, @RequestParam int following) {
        int affected = followCommandService.removeFollowing(me, following);
        return (affected > 0) ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
