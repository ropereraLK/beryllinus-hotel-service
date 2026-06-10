package com.beryllinus.backend.configuration;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka.topics")
public record KafkaTopicConfiguration(
        String booking,
        String payment,
        String inventory
) {
}
