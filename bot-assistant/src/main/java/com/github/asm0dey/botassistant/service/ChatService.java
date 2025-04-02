package com.github.asm0dey.botassistant.service;

import com.github.asm0dey.botassistant.model.ChatMessage;

/**
 * Service for processing chat messages and generating responses.
 */
public interface ChatService {

    /**
     * Process a chat message and generate a response.
     *
     * @param message the message to process
     * @return the generated response
     */
    ChatMessage processMessage(ChatMessage message);

    /**
     * Get statistics about the chat service.
     *
     * @return a string containing statistics
     */
    String getStatistics();
}