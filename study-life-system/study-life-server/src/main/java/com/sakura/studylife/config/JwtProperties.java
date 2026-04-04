package com.sakura.studylife.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "study-life.jwt")
public class JwtProperties {

    private String secret;

    private Long expireSeconds;

    private String issuer;
}
