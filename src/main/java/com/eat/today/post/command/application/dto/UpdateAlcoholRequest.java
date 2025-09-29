package com.eat.today.post.command.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAlcoholRequest {
    private String alcoholType;
    private String alcoholExplain;
    private String alcoholPicture;
}
