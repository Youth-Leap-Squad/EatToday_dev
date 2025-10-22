package com.eat.today.qna_rounge_report.report.query.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.eat.today.qna_rounge_report.report.query.repository")
public class ReportMybatisConfig {
}
