// BookmarkDTO.java
package com.eat.today.post.query.dto;
import lombok.Data;

@Data
public class BookmarkDTO {
    private String favorites;     // 컬럼 타입에 맞게 String/Integer/Boolean 중 선택
    private String memberId;
    private String boardTitle;
    private String foodPicture;
}
