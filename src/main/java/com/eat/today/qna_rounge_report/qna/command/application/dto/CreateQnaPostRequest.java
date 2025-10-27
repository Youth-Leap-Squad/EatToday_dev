package com.eat.today.qna_rounge_report.qna.command.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateQnaPostRequest( @NotNull Integer memberNo ,@NotBlank String inquiryTitle, @NotBlank String inquiryContent, @NotBlank String inquiryAt) {

}
