package com.sakura.studylife.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.sakura.studylife.mapper")
public class MybatisConfig {
}
