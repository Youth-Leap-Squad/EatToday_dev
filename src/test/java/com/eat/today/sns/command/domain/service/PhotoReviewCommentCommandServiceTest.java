package com.eat.today.sns.command.domain.service;

import com.eat.today.sns.command.application.entity.photoReviewComment.PhotoReviewCommentEntity;
import com.eat.today.sns.command.domain.repository.PhotoReviewComment.PhotoReviewCommentRepository;
import com.eat.today.sns.query.dto.photoReviewComment.PrcDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PhotoReviewCommentCommandServiceTest {

    private PhotoReviewCommentRepository repo;
    private PhotoReviewCommentCommandService service;

    @BeforeEach
    void setUp() {
        repo = mock(PhotoReviewCommentRepository.class);
        service = new PhotoReviewCommentCommandService(repo);
    }

    @Test
    void create_정상저장_아이디반환_필드세팅검증() {
        // given
        PrcDTO.CreateRequest req = mock(PrcDTO.CreateRequest.class);
        when(req.getDetail()).thenReturn("첫 댓글");

        when(repo.save(any(PhotoReviewCommentEntity.class))).thenAnswer(inv -> {
            PhotoReviewCommentEntity e = inv.getArgument(0);
            e.setPrcNo(123);
            return e;
        });

        // when
        PrcDTO.CreateResponse res = service.create(10, 20, req);

        // then
        assertThat(res.getPrcNo()).isEqualTo(123);

        ArgumentCaptor<PhotoReviewCommentEntity> captor =
                ArgumentCaptor.forClass(PhotoReviewCommentEntity.class);
        verify(repo).save(captor.capture());

        PhotoReviewCommentEntity saved = captor.getValue();
        assertThat(saved.getMemberNo()).isEqualTo(10);
        assertThat(saved.getReviewNo()).isEqualTo(20);
        assertThat(saved.getPrcDetail()).isEqualTo("첫 댓글");
        assertThat(saved.getPrcAt()).isNotNull();     // 시간 문자열 세팅 확인
        assertThat(saved.getPrcDeleted()).isFalse();
    }

    @Test
    void edit_존재하며_내댓글이면_내용수정되고_1반환() {
        // given
        PhotoReviewCommentEntity existing = new PhotoReviewCommentEntity();
        existing.setPrcNo(5);
        existing.setMemberNo(10);
        existing.setReviewNo(20);
        existing.setPrcDetail("old");
        existing.setPrcDeleted(false);

        when(repo.findByPrcNoAndMemberNoAndPrcDeletedFalse(5, 10))
                .thenReturn(Optional.of(existing));
        when(repo.save(any(PhotoReviewCommentEntity.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        PrcDTO.UpdateRequest req = mock(PrcDTO.UpdateRequest.class);
        when(req.getDetail()).thenReturn("new detail");

        // when
        int affected = service.edit(10, 5, req);

        // then
        assertThat(affected).isEqualTo(1);
        assertThat(existing.getPrcDetail()).isEqualTo("new detail");
        assertThat(existing.getPrcAt()).isNotNull();
        verify(repo).save(existing);
    }

    @Test
    void edit_대상없으면_0반환() {
        when(repo.findByPrcNoAndMemberNoAndPrcDeletedFalse(5, 10))
                .thenReturn(Optional.empty());
        PrcDTO.UpdateRequest req = mock(PrcDTO.UpdateRequest.class);

        int affected = service.edit(10, 5, req);

        assertThat(affected).isEqualTo(0);
        verify(repo, never()).save(any());
    }

    @Test
    void deleteHard_존재하면_delete호출_1반환() {
        PhotoReviewCommentEntity existing = new PhotoReviewCommentEntity();
        existing.setPrcNo(5);
        existing.setMemberNo(10);
        existing.setPrcDeleted(false);

        when(repo.findByPrcNoAndMemberNoAndPrcDeletedFalse(5, 10))
                .thenReturn(Optional.of(existing));

        int affected = service.deleteHard(10, 5);

        assertThat(affected).isEqualTo(1);
        verify(repo).delete(existing);
    }

    @Test
    void deleteHard_대상없으면_0반환() {
        when(repo.findByPrcNoAndMemberNoAndPrcDeletedFalse(5, 10))
                .thenReturn(Optional.empty());

        int affected = service.deleteHard(10, 5);

        assertThat(affected).isEqualTo(0);
        verify(repo, never()).delete(any());
    }

    @Test
    void deleteSoft_존재하면_플래그세팅_1반환() {
        PhotoReviewCommentEntity existing = new PhotoReviewCommentEntity();
        existing.setPrcNo(5);
        existing.setMemberNo(10);
        existing.setPrcDeleted(false);

        when(repo.findByPrcNoAndMemberNoAndPrcDeletedFalse(5, 10))
                .thenReturn(Optional.of(existing));

        int affected = service.deleteSoft(10, 5);

        assertThat(affected).isEqualTo(1);
        assertThat(existing.getPrcDeleted()).isTrue();
        // JPA 더티 체킹을 가정하므로 save 호출 없음
        verify(repo, never()).delete(any());
        verify(repo, never()).save(any());
    }

    @Test
    void deleteSoft_대상없으면_0반환() {
        when(repo.findByPrcNoAndMemberNoAndPrcDeletedFalse(5, 10))
                .thenReturn(Optional.empty());

        int affected = service.deleteSoft(10, 5);

        assertThat(affected).isEqualTo(0);
        verify(repo, never()).save(any());
    }
}
