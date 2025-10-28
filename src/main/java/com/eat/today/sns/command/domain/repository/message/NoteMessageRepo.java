package com.eat.today.sns.command.domain.repository.message;

import com.eat.today.sns.command.application.entity.message.NoteMessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteMessageRepo extends JpaRepository<NoteMessageEntity,Integer> {
    Page<NoteMessageEntity> findByReceiverNoAndReceiverDeletedFalseOrderBySentAtTxtDesc(Integer receiverNo, Pageable p);
    Page<NoteMessageEntity> findBySenderNoAndSenderDeletedFalseOrderBySentAtTxtDesc(Integer senderNo, Pageable p);
}