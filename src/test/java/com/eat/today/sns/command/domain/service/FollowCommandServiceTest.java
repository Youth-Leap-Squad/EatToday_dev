package com.eat.today.sns.command.domain.service;

import com.eat.today.sns.command.application.entity.follow.FollowEntity;
import com.eat.today.sns.command.domain.repository.follow.FollowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FollowCommandServiceTest {

    private FollowRepository followRepository;
    private FollowCommandService service;

    @BeforeEach
    void setUp() {
        followRepository = mock(FollowRepository.class);
        service = new FollowCommandService(followRepository, followRepository);
    }

    @Test
    void 자기자신을_팔로우하면_예외발생() {
        assertThatThrownBy(() -> service.follow(1, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("자기 자신은 팔로우할 수 없습니다.");
    }

    @Test
    void 이미팔로우중이면_save호출안함() {
        when(followRepository.existsByFollowerNoAndFollowingNo(1, 2)).thenReturn(true);

        service.follow(1, 2);

        verify(followRepository, never()).save(any(FollowEntity.class));
    }

    @Test
    void 새로운팔로우이면_save호출() {
        when(followRepository.existsByFollowerNoAndFollowingNo(1, 2)).thenReturn(false);

        service.follow(1, 2);

        ArgumentCaptor<FollowEntity> captor = ArgumentCaptor.forClass(FollowEntity.class);
        verify(followRepository, times(1)).save(captor.capture());

        FollowEntity saved = captor.getValue();
        assertThat(saved.getFollowerNo()).isEqualTo(1);
        assertThat(saved.getFollowingNo()).isEqualTo(2);
    }

    @Test
    void removeFollower_정상삭제() {
        when(followRepository.deleteByFollowerNoAndFollowingNo(2, 1)).thenReturn(1);

        int affected = service.removeFollower(1, 2);

        assertThat(affected).isEqualTo(1);
    }

    @Test
    void removeFollowing_정상삭제() {
        when(followRepository.deleteByFollowerNoAndFollowingNo(1, 2)).thenReturn(1);

        int affected = service.removeFollowing(1, 2);

        assertThat(affected).isEqualTo(1);
    }
}
