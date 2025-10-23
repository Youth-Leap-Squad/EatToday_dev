package com.eat.today.sns.command.domain.repository.message;

import com.eat.today.sns.command.application.entity.message.DmFileUploadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteFileUploadRepo extends JpaRepository<DmFileUploadEntity,Integer> {
    List<DmFileUploadEntity> findByNoteId(Integer noteId);
}