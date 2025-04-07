/*
 * Copyright © 2025 BellSoft (info@bell-sw.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
