package com.eat.today.member.command.application.dto;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class CommandHelloDTO {
    @Value("${hello.message}")  // 스프링의 환경설정(yml)에 적어둔 값을 이 필드에 주입해라.
    private String message;
}
