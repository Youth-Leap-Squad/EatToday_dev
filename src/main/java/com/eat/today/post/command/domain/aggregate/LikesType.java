package com.eat.today.post.command.domain.aggregate;

import lombok.Getter;

@Getter
public enum LikesType {

    LIKE_1("술술 들어가요"),
    LIKE_2("참신해요"),
    LIKE_3("맛없어요"),
    LIKE_4("궁금해요");

    public static final String DB_LIKE_1 = "술술 들어가요";
    public static final String DB_LIKE_2 = "참신해요";
    public static final String DB_LIKE_3 = "맛없어요";
    public static final String DB_LIKE_4 = "궁금해요";

    private final String dbValue;

    LikesType(String dbValue) { this.dbValue = dbValue; }

    public static LikesType fromDbValue(String v) {
        if (v == null) return null;
        switch (v) {
            case DB_LIKE_1: return LIKE_1;
            case DB_LIKE_2: return LIKE_2;
            case DB_LIKE_3: return LIKE_3;
            case DB_LIKE_4: return LIKE_4;
            default: throw new IllegalArgumentException("지원하지 않는 반응 타입입니다: [" + v + "]");
        }
    }

    public static LikesType fromNumber(Integer n) {
        if (n == null) throw new IllegalArgumentException("likesType 값이 누락되었습니다.");
        return switch (n) {
            case 1 -> LIKE_1;
            case 2 -> LIKE_2;
            case 3 -> LIKE_3;
            case 4 -> LIKE_4;
            default -> throw new IllegalArgumentException("likesType 형식이 올바르지 않습니다: " + n);
        };
    }
}
