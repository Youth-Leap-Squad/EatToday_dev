package com.eat.today.member.query.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReportDTO {

    private int reportNo;   // PK
    private int memberNo;   // FK
    private int memberNo2;  // FK

    private String reportTitle;
    private String reportContent;
    private String reportDate;

    @JsonIgnore
    private boolean reportYn;
    @JsonIgnore
    private String reportSource;
}
