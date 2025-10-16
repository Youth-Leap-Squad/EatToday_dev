package com.eat.today.qna_rounge_report.qna.query.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.eat.today.qna_rounge_report.qna.query.repository")
public class QnaMybatisConfig {
}
