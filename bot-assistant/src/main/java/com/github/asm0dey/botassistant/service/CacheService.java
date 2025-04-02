package com.github.asm0dey.botassistant.service;

import com.github.asm0dey.botassistant.model.ChatMessage;

import java.util.Optional;

/**
 * Service for caching frequently asked questions and responses.
 */
public interface CacheService {

    /**
     * Get a cached response for a message.
     *
     * @param message the message to get a response for
     * @return an Optional containing the cached response if found
     */
    Optional<ChatMessage> getCachedResponse(ChatMessage message);

    /**
     * Cache a response for a message.
     *
     * @param message the message
     * @param response the response to cache
     */
    void cacheResponse(ChatMessage message, ChatMessage response);

    /**
     * Clear the cache.
     */
    void clearCache();

    /**
     * Get the cache hit ratio.
     *
     * @return the cache hit ratio (hits / total requests)
     */
    double getCacheHitRatio();
}