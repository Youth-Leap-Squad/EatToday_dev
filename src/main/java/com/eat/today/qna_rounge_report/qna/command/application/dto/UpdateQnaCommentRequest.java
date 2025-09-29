package com.eat.today.qna_rounge_report.qna.command.application.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateQnaCommentRequest(@NotBlank String comment) {}