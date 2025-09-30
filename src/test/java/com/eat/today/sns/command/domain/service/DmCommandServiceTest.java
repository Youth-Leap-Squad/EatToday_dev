package com.eat.today.sns.command.domain.service;

import com.eat.today.sns.command.application.entity.dm.DmEntity;
import com.eat.today.sns.command.domain.repository.dm.DmRepository;
import com.eat.today.sns.query.dto.dm.DmUpdateDTO;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DmCommandServiceTest {

    private DmRepository dmRepository;
    private DmCommandService service;

    @BeforeEach
    void setUp() {
        dmRepository = mock(DmRepository.class);
        service = new DmCommandService(dmRepository);
    }

    @Test
    void sendDm_정상저장_필드세팅검증() {
        // given
        when(dmRepository.save(any(DmEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        DmEntity saved = service.sendDm(10, 20, "hello");

        // then
        ArgumentCaptor<DmEntity> captor = ArgumentCaptor.forClass(DmEntity.class);
        verify(dmRepository, times(1)).save(captor.capture());

        DmEntity toSave = captor.getValue();
        assertThat(toSave.getSendMemberId()).isEqualTo(10);
        assertThat(toSave.getReceiveMemberId()).isEqualTo(20);
        assertThat(toSave.getDmContent()).isEqualTo("hello");
        assertThat(toSave.getDmRead()).isFalse();
        assertThat(toSave.getDmDate()).isNotNull(); // now() 문자열 세팅 확인
        // 반환값도 동일 객체인지 간단 확인
        assertThat(saved).isSameAs(toSave);
    }

    @Test
    void updateDm_성공_콘텐츠수정() {
        // given
        DmEntity existing = new DmEntity();
        existing.setSendMemberId(11);
        existing.setReceiveMemberId(22);
        existing.setDmContent("old");

        when(dmRepository.findById(999)).thenReturn(Optional.of(existing));
        when(dmRepository.save(any(DmEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DmUpdateDTO req = mock(DmUpdateDTO.class);
        when(req.getSender()).thenReturn(11);
        when(req.getContent()).thenReturn("new content");

        // when
        int affected = service.updateDm(999, req);

        // then
        assertThat(affected).isEqualTo(1);
        ArgumentCaptor<DmEntity> captor = ArgumentCaptor.forClass(DmEntity.class);
        verify(dmRepository).save(captor.capture());
        assertThat(captor.getValue().getDmContent()).isEqualTo("new content");
    }

    @Test
    void updateDm_보낸사람불일치_예외() {
        // given
        DmEntity existing = new DmEntity();
        existing.setSendMemberId(11);
        existing.setDmContent("old");
        when(dmRepository.findById(1)).thenReturn(Optional.of(existing));

        DmUpdateDTO req = mock(DmUpdateDTO.class);
        when(req.getSender()).thenReturn(99); // 다른 사용자
        when(req.getContent()).thenReturn("try hack");

        // then
        assertThatThrownBy(() -> service.updateDm(1, req))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("본인이 보낸 DM만 수정할 수 있습니다.");
        verify(dmRepository, never()).save(any());
    }

    @Test
    void updateDm_대상없음_EntityNotFound() {
        // given
        when(dmRepository.findById(12345)).thenReturn(Optional.empty());
        DmUpdateDTO req = mock(DmUpdateDTO.class);

        // then
        assertThatThrownBy(() -> service.updateDm(12345, req))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("message_no=12345");
        verify(dmRepository, never()).save(any());
    }

    @Test
    void updateDm_content_null_내용유지() {
        // given
        DmEntity existing = new DmEntity();
        existing.setSendMemberId(7);
        existing.setDmContent("keep-this");
        when(dmRepository.findById(2)).thenReturn(Optional.of(existing));
        when(dmRepository.save(any(DmEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DmUpdateDTO req = mock(DmUpdateDTO.class);
        when(req.getSender()).thenReturn(7);
        when(req.getContent()).thenReturn(null); // 내용 미변경

        // when
        int affected = service.updateDm(2, req);

        // then
        assertThat(affected).isEqualTo(1);
        ArgumentCaptor<DmEntity> captor = ArgumentCaptor.forClass(DmEntity.class);
        verify(dmRepository).save(captor.capture());
        assertThat(captor.getValue().getDmContent()).isEqualTo("keep-this");
    }

    @Test
    void deleteReceived_개수반환() {
        when(dmRepository.deleteByReceiveMemberId(55)).thenReturn(3);
        assertThat(service.deleteReceived(55)).isEqualTo(3);
    }

    @Test
    void deleteSent_개수반환() {
        when(dmRepository.deleteBySendMemberId(77)).thenReturn(5);
        assertThat(service.deleteSent(77)).isEqualTo(5);
    }

    @Test
    void deleteMessage_repo호출검증() {
        service.deleteMessage(9999);
        verify(dmRepository, times(1)).deleteById(9999);
    }
}
