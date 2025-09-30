package com.eat.today.sns.command.domain.service;

import com.eat.today.sns.command.application.entity.dm.DmEntity;
import com.eat.today.sns.command.domain.repository.dm.DmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DmService {
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