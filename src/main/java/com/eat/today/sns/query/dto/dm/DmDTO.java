package com.eat.today.sns.query.dto.dm;

import lombok.Data;

@Data
public class DmDTO {
    private int messageNo;
    private int sendMemberId;
    private int receiveMemberId;
    private String dmContent;
    private String dmDate;
    private boolean dmRead;
}