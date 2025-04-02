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

}
