package com.github.asm0dey.botassistant.service;

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
    private final AtomicLong cacheHits = new AtomicLong(0);
    private final AtomicLong cacheMisses = new AtomicLong(0);

    public CacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
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

        if (cachedResponse instanceof ChatMessage) {
            log.info("Cache hit for message: {}", message.content());
            cacheHits.incrementAndGet();
            return Optional.of((ChatMessage) cachedResponse);
        } else {
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

        redisTemplate.opsForValue().set(key, response, CACHE_TTL_HOURS, TimeUnit.HOURS);
        log.info("Cached response for message: {}", message.content());
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
    private String generateCacheKey(ChatMessage message) {
        // Normalize the message content (lowercase, trim, remove extra spaces)
        String normalizedContent = message.content().toLowerCase().trim().replaceAll("\\s+", " ");
        return CACHE_KEY_PREFIX + normalizedContent;
    }
}
