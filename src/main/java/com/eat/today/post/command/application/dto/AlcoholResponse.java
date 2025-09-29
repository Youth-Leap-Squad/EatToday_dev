package com.eat.today.post.command.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlcoholResponse {
    private Integer alcoholNo;
    private String alcoholType;
    private String alcoholExplain;
    private String alcoholPicture;
}
