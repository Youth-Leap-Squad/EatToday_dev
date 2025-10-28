package com.eat.today.sns.query.dto.dm;

import lombok.Data;

@Data
public class DmFileDTO {
    private int dmFileId;
    private int noteId;
    private String dmFileName;
    private String dmFileType;
    private String dmFileRename;
    private String dmFilePath;
    private long dmFileSize;
    private String dmFileAt;
    private String dmFileUrl;
}
