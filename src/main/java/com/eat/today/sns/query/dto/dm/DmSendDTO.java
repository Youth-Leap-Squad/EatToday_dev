package com.eat.today.sns.query.dto.dm;

import lombok.Data;

@Data
public class DmSendDTO {
    private int sender;
    private int receiver;
    private String content;
}
