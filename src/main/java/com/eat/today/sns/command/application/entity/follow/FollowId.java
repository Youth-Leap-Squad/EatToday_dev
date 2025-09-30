package com.eat.today.sns.command.application.entity.follow;

import lombok.*;
import java.io.Serializable;

@Data
public class FollowId implements Serializable {
    private int followerNo;
    private int followingNo;
}

