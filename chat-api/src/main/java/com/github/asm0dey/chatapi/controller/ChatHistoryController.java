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
package com.github.asm0dey.chatapi.controller;

import com.github.asm0dey.chatapi.model.ChatMessage;
import com.github.asm0dey.chatapi.repository.ChatMessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for retrieving chat history.
 */
@RestController
@RequestMapping("/api/chat/history")
public class ChatHistoryController {

    private final ChatMessageRepository chatMessageRepository;

    public ChatHistoryController(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    /**
     * Get all messages across all sessions (admin only).
     *
     * @return a list of all chat messages
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ChatMessage>> getAllMessages() {
        List<ChatMessage> messages = chatMessageRepository.findAll();
        return ResponseEntity.ok(messages);
    }

    /**
     * Get all messages for a specific chat session.
     *
     * @param sessionId the session ID
     * @return a list of chat messages
     */
    @GetMapping("/{sessionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ChatMessage>> getChatHistory(@PathVariable String sessionId) {
        List<ChatMessage> messages = chatMessageRepository.findBySessionId(sessionId);
        return ResponseEntity.ok(messages);
    }

    /**
     * Get paginated messages for a specific chat session.
     *
     * @param sessionId the session ID
     * @param page      the page number (0-based)
     * @param size      the page size
     * @return a page of chat messages
     */
    @GetMapping("/{sessionId}/page")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<ChatMessage>> getChatHistoryPaginated(
            @PathVariable String sessionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<ChatMessage> messages = chatMessageRepository.findBySessionId(sessionId, pageRequest);
        return ResponseEntity.ok(messages);
    }

    /**
     * Get messages for a specific chat session after a given timestamp.
     *
     * @param sessionId the session ID
     * @param timestamp the timestamp to filter messages after
     * @return a list of chat messages
     */
    @GetMapping("/{sessionId}/after")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ChatMessage>> getChatHistoryAfter(
            @PathVariable String sessionId,
            @RequestParam LocalDateTime timestamp) {

        List<ChatMessage> messages = chatMessageRepository.findBySessionIdAndTimestampAfter(sessionId, timestamp);
        return ResponseEntity.ok(messages);
    }

    /**
     * Get the count of messages in a specific chat session.
     *
     * @param sessionId the session ID
     * @return the count of messages
     */
    @GetMapping("/{sessionId}/count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> getChatMessageCount(@PathVariable String sessionId) {
        long count = chatMessageRepository.countBySessionId(sessionId);
        return ResponseEntity.ok(count);
    }
}
