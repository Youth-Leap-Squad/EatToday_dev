package com.eat.today.sns.query.dto.dm;

import lombok.Data;
import java.util.List;

@Data
public class MessageDTO {
    private int noteId;
    private int senderNo;
    private int receiverNo;
    private String subject;
    private String content;
    private String sentAtTxt;
    private boolean read;
    private String readAtTxt;

    // 첨부 파일
    private List<DmFileDTO> files;
}
