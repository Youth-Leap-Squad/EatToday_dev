package com.eat.today.member.command.domain.aggregate;

/**
 * 회원 활동별 포인트 정책
 */
public enum PointPolicy {
    
    LOGIN(10, "로그인"),
    POST_CREATE(20, "게시물 등록"),
    COMMENT_CREATE(5, "댓글 작성"),
    ALBTI_PARTICIPATE(30, "술BTI 이벤트 참여"),
    WORLDCUP_PARTICIPATE(30, "월드컵 게임 참여"),
    PHOTO_REVIEW_CREATE(25, "사진 리뷰 작성");

    private final Integer points;
    private final String description;

    PointPolicy(Integer points, String description) {
        this.points = points;
        this.description = description;
    }

    public Integer getPoints() {
        return points;
    }

    public String getDescription() {
        return description;
    }
}

