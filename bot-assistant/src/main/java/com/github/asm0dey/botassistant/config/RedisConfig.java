package com.github.asm0dey.botassistant.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisConnectionDetails;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Redis configuration for caching bot responses.
 */
@Configuration
public class RedisConfig {

    @Value("${spring.cache.redis.time-to-live:3600}")
    private long timeToLive;

    /**
     * Redis connection factory.
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory(RedisConnectionDetails redisConnectionDetails) {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(redisConnectionDetails.getStandalone().getHost());
        redisConfig.setPort(redisConnectionDetails.getStandalone().getPort());
        if (redisConnectionDetails.getPassword() != null && !redisConnectionDetails.getPassword().isEmpty()) {
            redisConfig.setPassword(redisConnectionDetails.getPassword());
        }
        return new LettuceConnectionFactory(redisConfig);
    }

    /**
     * Redis template for operations.
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionDetails redisConnectionDetails) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory(redisConnectionDetails));
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    /**
     * Cache manager for Redis.
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory, RedisConnectionDetails redisConnectionDetails) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(timeToLive))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues();

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }
}
