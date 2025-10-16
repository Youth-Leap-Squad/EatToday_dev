package com.eat.today.qna_rounge_report.rounge.query.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.eat.today.qna_rounge_report.rounge.query.repository")
public class RoungeMybatisConfig {
}
