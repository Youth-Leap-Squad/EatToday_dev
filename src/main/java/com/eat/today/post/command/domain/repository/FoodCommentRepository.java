package com.eat.today.post.command.domain.repository;

import com.eat.today.post.command.domain.aggregate.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodCommentRepository extends JpaRepository<FoodComment, Integer> {}