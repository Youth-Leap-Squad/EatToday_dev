package com.eat.today.sns.command.domain.service;

import com.eat.today.sns.command.application.dto.photoReviewDTO.CreateRequest;
import com.eat.today.sns.command.application.dto.photoReviewDTO.UpdateRequest;
import com.eat.today.sns.command.application.entity.photoReview.PhotoReviewEntity;
import com.eat.today.sns.command.domain.repository.photoReview.PhotoReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PhotoReviewCommandServiceTest {

    private PhotoReviewRepository repository;
    private PhotoReviewCommandService service;

    @BeforeEach
    void setUp() {
        repository = mock(PhotoReviewRepository.class);
        service = new PhotoReviewCommandService(repository);
    }

    @Test
    void create_정상저장_리뷰번호반환() {
        // given: DTO 목과 필드 스텁
        CreateRequest req = mock(CreateRequest.class);
        when(req.getBoardNo()).thenReturn(101);
        when(req.getMemberNo()).thenReturn(202);
        when(req.getReviewTitle()).thenReturn("제목");
        when(req.getReviewDate()).thenReturn("2025-09-30");
        when(req.getReviewContent()).thenReturn("내용");
        when(req.getReviewLike()).thenReturn(7);

        // save가 reviewNo 채워서 반환하도록 스텁
        when(repository.save(any(PhotoReviewEntity.class))).thenAnswer(invocation -> {
            PhotoReviewEntity e = invocation.getArgument(0);
            e.setReviewNo(1000);
            return e;
        });

        // when
        int reviewNo = service.create(req);

        // then
        assertThat(reviewNo).isEqualTo(1000);

        ArgumentCaptor<PhotoReviewEntity> captor = ArgumentCaptor.forClass(PhotoReviewEntity.class);
        verify(repository).save(captor.capture());
        PhotoReviewEntity saved = captor.getValue();
        assertThat(saved.getBoardNo()).isEqualTo(101);
        assertThat(saved.getMemberNo()).isEqualTo(202);
        assertThat(saved.getReviewTitle()).isEqualTo("제목");
        assertThat(saved.getReviewDate()).isEqualTo("2025-09-30");
        assertThat(saved.getReviewContent()).isEqualTo("내용");
        assertThat(saved.getReviewLike()).isEqualTo(7);
    }

    @Test
    void edit_존재하지않으면_EntityNotFound() {
        when(repository.findById(9999)).thenReturn(Optional.empty());
        UpdateRequest req = mock(UpdateRequest.class);

        assertThatThrownBy(() -> service.edit(9999, req))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("review_no=9999");

        verify(repository, never()).save(any());
    }

    @Test
    void edit_부분업데이트_널아닌필드만반영() {
        // given: 기존 엔티티
        PhotoReviewEntity existing = new PhotoReviewEntity();
        existing.setReviewNo(10);
        existing.setBoardNo(1);
        existing.setMemberNo(2);
        existing.setReviewTitle("old title");
        existing.setReviewDate("2025-01-01");
        existing.setReviewContent("old content");
        existing.setReviewLike(0);

        when(repository.findById(10)).thenReturn(Optional.of(existing));
        when(repository.save(any(PhotoReviewEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 업데이트 요청: 일부만 값 제공(나머지는 null)
        UpdateRequest req = mock(UpdateRequest.class);
        when(req.getBoardNo()).thenReturn(null);          // 변경 X
        when(req.getMemberNo()).thenReturn(222);          // 변경 O
        when(req.getReviewTitle()).thenReturn("new title"); // 변경 O
        when(req.getReviewDate()).thenReturn(null);       // 변경 X
        when(req.getReviewContent()).thenReturn("new content"); // 변경 O
        when(req.getReviewLike()).thenReturn(5);          // 변경 O

        // when
        int affected = service.edit(10, req);

        // then
        assertThat(affected).isEqualTo(1);

        ArgumentCaptor<PhotoReviewEntity> captor = ArgumentCaptor.forClass(PhotoReviewEntity.class);
        verify(repository).save(captor.capture());
        PhotoReviewEntity updated = captor.getValue();

        // 변경된 것
        assertThat(updated.getMemberNo()).isEqualTo(222);
        assertThat(updated.getReviewTitle()).isEqualTo("new title");
        assertThat(updated.getReviewContent()).isEqualTo("new content");
        assertThat(updated.getReviewLike()).isEqualTo(5);
        // 변경되지 않은 것 유지
        assertThat(updated.getBoardNo()).isEqualTo(1);
        assertThat(updated.getReviewDate()).isEqualTo("2025-01-01");
    }

    @Test
    void delete_대상없으면_0반환() {
        when(repository.existsById(123)).thenReturn(false);

        int result = service.delete(123);

        assertThat(result).isEqualTo(0);
        verify(repository, never()).deleteById(anyInt());
    }

    @Test
    void delete_대상있으면_삭제후_1반환() {
        when(repository.existsById(321)).thenReturn(true);

        int result = service.delete(321);

        assertThat(result).isEqualTo(1);
        verify(repository, times(1)).deleteById(321);
    }
}
