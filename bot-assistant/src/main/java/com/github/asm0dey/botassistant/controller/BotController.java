/*
 * Copyright 2025 BellSoft (info@bell-sw.com)
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
package com.github.asm0dey.botassistant.controller;

/*-
 * #%L
 * hero-guide-demo-app
 * %%
 * Copyright (C) 2025 BellSoft
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
