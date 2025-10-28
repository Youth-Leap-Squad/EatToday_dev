package com.eat.today.common.jpa;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false) // 엔티티 필드에 명시적으로 붙여줄 예정
public class BooleanTFConverter implements AttributeConverter<Boolean, String> {
    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        // null은 'F'로 통일 (원하면 null 유지도 가능)
        return Boolean.TRUE.equals(attribute) ? "T" : "F";
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        if (dbData == null) return Boolean.FALSE;
        return "T".equalsIgnoreCase(dbData) || "Y".equalsIgnoreCase(dbData) || "1".equals(dbData) || "TRUE".equalsIgnoreCase(dbData);
    }
}
