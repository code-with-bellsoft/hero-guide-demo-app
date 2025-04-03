package com.github.asm0dey.botassistant.service;

import com.github.asm0dey.botassistant.model.ChatMessage;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class CacheServiceTest {
    @Container
    @ServiceConnection
    static RedisContainer redis = new RedisContainer("redis");

    @Autowired
    private CacheService cacheService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    void setUp() {
        // Clear the cache before each test
        cacheService.clearCache();
    }

    @Test
    void testCacheResponseAndRetrieval() {
        // Create a test message
        ChatMessage message = new ChatMessage(
                "test-id",
                "test-session",
                "user-1",
                "Test User",
                ChatMessage.MessageType.CHAT,
                "Hello, world!",
                LocalDateTime.now(),
                false
        );

        // Create a test response
        ChatMessage response = new ChatMessage(
                "response-id",
                "test-session",
                "bot",
                "Bot Assistant",
                ChatMessage.MessageType.BOT,
                "Hello, Test User!",
                LocalDateTime.now(),
                true
        );

        // Cache the response
        cacheService.cacheResponse(message, response);

        // Retrieve the cached response
        Optional<ChatMessage> cachedResponse = cacheService.getCachedResponse(message);

        // Verify that the response was cached and retrieved correctly
        assertTrue(cachedResponse.isPresent(), "Cached response should be present");
        assertEquals(response.content(), cachedResponse.get().content(), "Cached response content should match original");
        assertEquals(response.type(), cachedResponse.get().type(), "Cached response type should match original");
    }
}
