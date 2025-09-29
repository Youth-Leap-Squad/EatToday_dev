package com.eat.today.post.command.domain.repository;

import com.eat.today.post.command.domain.aggregate.Bookmark;
import com.eat.today.post.command.domain.aggregate.BookmarkId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkId> {
    List<Bookmark> findAllByMember_MemberNo(Integer memberNo);
}
