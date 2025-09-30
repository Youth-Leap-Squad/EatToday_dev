package com.eat.today.sns.command.application.entity.follow;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Table(name = "follow")
@IdClass(FollowId.class)
public class FollowEntity {
    @Id
    @Column(name = "follower_no", nullable = false)
    private int followerNo;

    @Id
    @Column(name = "following_no", nullable = false)
    private int followingNo;

    @Column(name = "created_at", nullable = true, length = 50)
    private String createdAt;
}