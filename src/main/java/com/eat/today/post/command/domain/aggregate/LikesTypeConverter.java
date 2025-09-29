package com.eat.today.post.command.domain.aggregate;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class LikesTypeConverter implements AttributeConverter<LikesType, String> {

    @Override
    public String convertToDatabaseColumn(LikesType attribute) {
        if (attribute == null) return null;
        switch (attribute) {
            case LIKE_1: return LikesType.DB_LIKE_1;
            case LIKE_2: return LikesType.DB_LIKE_2;
            case LIKE_3: return LikesType.DB_LIKE_3;
            case LIKE_4: return LikesType.DB_LIKE_4;
            default: throw new IllegalArgumentException("알 수 없는 반응 타입입니다: " + attribute);
        }
    }

    @Override
    public LikesType convertToEntityAttribute(String dbData) {
        return LikesType.fromDbValue(dbData);
    }
}
