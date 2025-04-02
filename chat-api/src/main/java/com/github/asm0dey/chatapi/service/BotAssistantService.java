package com.github.asm0dey.chatapi.service;

import com.github.asm0dey.chatapi.model.ChatMessage;

/**
 * Service for interacting with the Bot Assistant.
 */
public interface BotAssistantService {

    /**
     * Process a chat message with the bot assistant.
     *
     * @param message the message to process
     */
    void processMessage(ChatMessage message);
}