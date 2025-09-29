package com.eat.today.post.command.domain.repository;

import com.eat.today.post.command.domain.aggregate.FoodPostLike;
import com.eat.today.post.command.domain.aggregate.FoodPostLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodPostLikeRepository extends JpaRepository<FoodPostLike, FoodPostLikeId> {}
