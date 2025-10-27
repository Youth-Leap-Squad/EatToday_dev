package com.eat.today.post.command.domain.repository;

import com.eat.today.post.command.domain.aggregate.BookmarkFolder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkFolderRepository extends JpaRepository<BookmarkFolder, Integer> {
    List<BookmarkFolder> findAllByMember_MemberNo(Integer memberNo);
    Optional<BookmarkFolder> findByMember_MemberNoAndFolderName(Integer memberNo, String folderName);
}
