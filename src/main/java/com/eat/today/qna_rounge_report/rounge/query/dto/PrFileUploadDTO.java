package com.eat.today.qna_rounge_report.rounge.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PrFileUploadDTO {
    private Integer prFileNo;
    private String prFileName;
    private String prFileType;
    private String prFileRename;
    private String prFilePath;
    private String prFileAt;
    private Integer reviewNo;
}
