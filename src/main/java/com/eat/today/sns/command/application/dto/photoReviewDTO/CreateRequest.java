package com.eat.today.sns.command.application.dto.photoReviewDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateRequest {
    @NotNull private Integer boardNo;
    @NotNull private Integer memberNo;
    @NotBlank private String  reviewTitle;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$",
            message = "reviewDate는 yyyy-MM-dd 형식이어야 합니다")
    private String  reviewDate;   // nullable

    @NotBlank private String  reviewContent;
    @NotNull  private Integer reviewLike;
}
