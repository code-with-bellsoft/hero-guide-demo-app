package com.github.asm0dey.botassistant.controller;

import com.github.asm0dey.botassistant.model.ChatMessage;
import com.github.asm0dey.botassistant.service.CacheService;
import com.github.asm0dey.botassistant.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for the Bot Assistant.
 */
@RestController
@RequestMapping("/api/bot")
public class BotController {
    private static final Logger log = LoggerFactory.getLogger(BotController.class);

    private final ChatService chatService;
    private final CacheService cacheService;

    public BotController(ChatService chatService, CacheService cacheService) {
        this.chatService = chatService;
        this.cacheService = cacheService;
    }

    /**
     * Process a chat message and generate a response.
     *
     * @param message the message to process
     * @return the generated response
     */
    @PostMapping("/process")
    public ResponseEntity<ChatMessage> processMessage(@RequestBody ChatMessage message) {
        log.info("Received message for processing: {}", message.content());
        ChatMessage response = chatService.processMessage(message);
        return ResponseEntity.ok(response);
    }

    /**
     * Get statistics about the bot service.
     *
     * @return statistics about the bot service
     */
    @GetMapping("/stats")
    public ResponseEntity<String> getStatistics() {
        log.info("Retrieving bot statistics");
        String stats = chatService.getStatistics();
        return ResponseEntity.ok(stats);
    }

    /**
     * Clear the response cache.
     *
     * @return a success message
     */
    @PostMapping("/cache/clear")
    public ResponseEntity<String> clearCache() {
        log.info("Clearing response cache");
        cacheService.clearCache();
        return ResponseEntity.ok("Cache cleared successfully");
    }

    /**
     * Get the cache hit ratio.
     *
     * @return the cache hit ratio
     */
    @GetMapping("/cache/hit-ratio")
    public ResponseEntity<Double> getCacheHitRatio() {
        log.info("Retrieving cache hit ratio");
        double hitRatio = cacheService.getCacheHitRatio();
        return ResponseEntity.ok(hitRatio);
    }

    /**
     * Health check endpoint.
     *
     * @return a success message
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Bot Assistant is running");
    }
}
