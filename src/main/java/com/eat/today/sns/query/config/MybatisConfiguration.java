package com.eat.today.sns.query.config;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.eat.today.sns.query", annotationClass = Mapper.class)
public class MybatisConfiguration {
    // ✅ 여기서는 보안 관련 빈을 만들지 않습니다.
    //    이 클래스는 MyBatis 매퍼 스캔만 담당.
}
