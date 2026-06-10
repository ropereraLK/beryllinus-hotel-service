package com.beryllinus.backend.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
@ConfigurationProperties(prefix = "cache")
@Getter
@Setter
public class RedisConfig {
    private Duration defaultTtl;
    private Duration roomClassesTtl;
}
