package com.eat.today.qna_rounge_report.rounge.query.dto;


import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoReviewPageResponse {
    private List<PhotoReviewDTO> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
}