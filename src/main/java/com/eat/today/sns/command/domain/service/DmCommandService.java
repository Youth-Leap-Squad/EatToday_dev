package com.eat.today.sns.command.domain.service;

import com.eat.today.sns.command.application.entity.dm.DmEntity;
import com.eat.today.sns.command.domain.repository.dm.DmRepository;
import com.eat.today.sns.query.dto.dm.DmUpdateDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DmCommandService {
    private final DmRepository dmRepository;

    // DM 전송
    @Transactional
    public DmEntity sendDm(int sender, int receiver, String content) {
        DmEntity dm = new DmEntity();
        dm.setSendMemberId(sender);
        dm.setReceiveMemberId(receiver);
        dm.setDmContent(content);
        dm.setDmDate(java.time.LocalDateTime.now().toString());
        dm.setDmRead(false);
        return dmRepository.save(dm);
    }

    @Transactional
    public int updateDm(int messageNo, DmUpdateDTO req) {
        DmEntity dm = dmRepository.findById(messageNo)
                .orElseThrow(() -> new EntityNotFoundException("message_no=" + messageNo));

        if (dm.getSendMemberId() != req.getSender()) {
            throw new IllegalStateException("본인이 보낸 DM만 수정할 수 있습니다.");
        }

        if (req.getContent() != null) {
            dm.setDmContent(req.getContent());
        }

        dmRepository.save(dm);
        return 1;
    }

    // 받은 메시지 전체 삭제
    @Transactional
    public int deleteReceived(int memberId) {
        return dmRepository.deleteByReceiveMemberId(memberId);
    }

    // 보낸 메시지 전체 삭제
    @Transactional
    public int deleteSent(int memberId) {
        return dmRepository.deleteBySendMemberId(memberId);
    }

    // 특정 메시지 하나 삭제
    @Transactional
    public void deleteMessage(int messageNo) {
        dmRepository.deleteById(messageNo);
    }
}