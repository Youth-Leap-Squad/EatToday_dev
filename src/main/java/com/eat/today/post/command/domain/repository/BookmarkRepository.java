package com.eat.today.post.command.domain.repository;

import com.eat.today.post.command.domain.aggregate.Bookmark;
import com.eat.today.post.command.domain.aggregate.BookmarkKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkKey> {
    List<Bookmark> findAllByFolder_FolderId(Integer folderId);
}