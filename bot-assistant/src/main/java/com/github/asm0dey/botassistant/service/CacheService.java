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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.asm0dey.botassistant.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Redis implementation of the CacheService.
 */
@Service
public class CacheService {
    private static final Logger log = LoggerFactory.getLogger(CacheService.class);

    private static final String CACHE_KEY_PREFIX = "bot:response:";
    private static final long CACHE_TTL_HOURS = 24;

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final AtomicLong cacheHits = new AtomicLong(0);
    private final AtomicLong cacheMisses = new AtomicLong(0);

    public CacheService(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Get a cached response for a message.
     *
     * @param message the message to get a response for
     * @return an Optional containing the cached response if found
     */
    public Optional<ChatMessage> getCachedResponse(ChatMessage message) {
        String key = generateCacheKey(message);
        log.debug("Looking up cached response for key: {}", key);

        Object cachedResponse = redisTemplate.opsForValue().get(key);

        if (cachedResponse == null) {
            log.info("Cache miss for message: {}", message.content());
            cacheMisses.incrementAndGet();
            return Optional.empty();
        }

        try {
            ChatMessage chatMessage = objectMapper.readValue(cachedResponse.toString(), ChatMessage.class);
            log.info("Cache hit for message: {}", message.content());
            cacheHits.incrementAndGet();
            return Optional.of(chatMessage);
        } catch (Exception e) {
            log.error("Error converting cached response to ChatMessage", e);
            log.info("Cache miss for message: {}", message.content());
            cacheMisses.incrementAndGet();
            return Optional.empty();
        }
    }

    /**
     * Cache a response for a message.
     *
     * @param message  the message
     * @param response the response to cache
     */
    public void cacheResponse(ChatMessage message, ChatMessage response) {
        String key = generateCacheKey(message);
        log.debug("Caching response for key: {}", key);

        try {
            // Serialize the response to a JSON string
            String jsonResponse = objectMapper.writeValueAsString(response);
            redisTemplate.opsForValue().set(key, jsonResponse, CACHE_TTL_HOURS, TimeUnit.HOURS);
            log.info("Cached response for message: {}", message.content());
        } catch (Exception e) {
            log.error("Error caching response", e);
        }
    }

    /**
     * Clear the cache.
     */
    public void clearCache() {
        log.info("Clearing cache");
        // Get all keys with the prefix
        redisTemplate.keys(CACHE_KEY_PREFIX + "*").forEach(redisTemplate::delete);

        // Reset statistics
        cacheHits.set(0);
        cacheMisses.set(0);
    }

    /**
     * Get the cache hit ratio.
     *
     * @return the cache hit ratio (hits / total requests)
     */
    public double getCacheHitRatio() {
        long hits = cacheHits.get();
        long total = hits + cacheMisses.get();

        if (total == 0) {
            return 0.0;
        }

        return (double) hits / total;
    }

    /**
     * Generate a cache key for a message.
     * We use a normalized version of the message content as the key.
     *
     * @param message the message
     * @return the cache key
     */
    public String generateCacheKey(ChatMessage message) {
        // Normalize the message content (lowercase, trim, remove extra spaces)
        String normalizedContent = message.content().toLowerCase().trim().replaceAll("\\s+", " ");
        return CACHE_KEY_PREFIX + normalizedContent;
    }

}
