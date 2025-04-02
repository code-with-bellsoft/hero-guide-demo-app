package com.github.asm0dey.botassistant.model;

import java.time.LocalDateTime;

/**
 * Represents a chat message in the system.
 * This class is compatible with the ChatMessage class in the chat-api module.
 * Implemented as a record for immutability and simplicity.
 */
public record ChatMessage(
    String id,
    String sessionId,
    String senderId,
    String senderName,
    MessageType type,
    String content,
    LocalDateTime timestamp,
    boolean processedByBot
) {
    /**
     * Types of messages that can be exchanged.
     */
    public enum MessageType {
        CHAT,       // Regular chat message
        JOIN,       // User joined notification
        LEAVE,      // User left notification
        BOT         // Message from bot assistant
    }

    /**
     * Builder method to create a new ChatMessage with the same values as this one
     * but with a different content.
     * 
     * @param content the new content
     * @return a new ChatMessage with updated content
     */
    public ChatMessage withContent(String content) {
        return new ChatMessage(id, sessionId, senderId, senderName, type, content, timestamp, processedByBot);
    }

    /**
     * Builder method to create a new ChatMessage with the same values as this one
     * but with a different processedByBot flag.
     * 
     * @param processedByBot the new processedByBot flag
     * @return a new ChatMessage with updated processedByBot flag
     */
    public ChatMessage withProcessedByBot(boolean processedByBot) {
        return new ChatMessage(id, sessionId, senderId, senderName, type, content, timestamp, processedByBot);
    }
}
