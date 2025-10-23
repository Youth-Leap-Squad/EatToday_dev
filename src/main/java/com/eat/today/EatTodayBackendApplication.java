package com.eat.today;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.eat.today.post.query.mapper")
public class EatTodayBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EatTodayBackendApplication.class, args);
    }

}