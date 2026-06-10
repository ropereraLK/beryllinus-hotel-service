package com.beryllinus.backend.beans;

import com.beryllinus.backend.configuration.RedisConfig;
import com.beryllinus.backend.constants.CacheConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CacheConfiguration {

    @Bean
    public RedisCacheManager cacheManager(
            RedisConnectionFactory connectionFactory,
            RedisConfig redisConfig) {

        RedisCacheConfiguration defaultConfig =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(redisConfig.getDefaultTtl());

        Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();

        cacheConfigs.put(
                CacheConstants.ROOM_CLASSES,
                defaultConfig.entryTtl(
                        redisConfig.getRoomClassesTtl()
                )
        );

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigs)
                .build();
    }
}