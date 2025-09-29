package com.eat.today.sns.query.dto.follow;

import com.eat.today.member.query.dto.MemberDTO;
import lombok.Data;


@Data
public class FollowDTO {
    private int followerNo;
    private int followingNo;
    private String createdAt;

    private MemberDTO member;        // association 매핑 대상

}
