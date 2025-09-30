package com.eat.today.sns.command.application.dto.photoReviewDTO;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateRequest {
    private Integer boardNo;
    private Integer memberNo;
    private String  reviewTitle;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$",
            message = "reviewDate는 yyyy-MM-dd 형식이어야 합니다")
    private String  reviewDate;

    private String  reviewContent;
    private Integer reviewLike;
}
