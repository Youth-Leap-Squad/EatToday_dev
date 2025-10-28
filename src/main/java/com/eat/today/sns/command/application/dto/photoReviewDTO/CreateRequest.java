package com.eat.today.sns.command.application.dto.photoReviewDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateRequest {
    @NotNull private Integer boardNo;
    @NotNull private Integer memberNo;
    @NotBlank private String reviewTitle;
    private String reviewDate; // ✅ 자동 생성하므로 필수 아님
    @NotBlank private String reviewContent;
    private Integer reviewLike;
}
