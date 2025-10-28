package com.eat.today.sns.query.config;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.eat.today.sns.query", annotationClass = Mapper.class)
public class MybatisConfiguration {
}
