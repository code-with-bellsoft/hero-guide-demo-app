package com.github.asm0dey.chatapi.controller;

import com.github.asm0dey.chatapi.model.ChatMessage;
import com.github.asm0dey.chatapi.model.ChatSession;
import com.github.asm0dey.chatapi.repository.ChatMessageRepository;
import com.github.asm0dey.chatapi.repository.ChatSessionRepository;
import com.github.asm0dey.chatapi.service.BotAssistantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Controller for handling WebSocket chat messages.
 */
@Controller
public class ChatWebSocketController {
    private static final Logger log = LoggerFactory.getLogger(ChatWebSocketController.class);

    private final ChatMessageRepository chatMessageRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final BotAssistantService botAssistantService;

    public ChatWebSocketController(ChatMessageRepository chatMessageRepository,
                                  ChatSessionRepository chatSessionRepository,
                                  BotAssistantService botAssistantService) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatSessionRepository = chatSessionRepository;
        this.botAssistantService = botAssistantService;
    }

    /**
     * Handle chat messages sent to a specific session.
     *
     * @param sessionId the session ID
     * @param chatMessage the chat message
     * @return the chat message to be broadcast to subscribers
     */
    @MessageMapping("/chat/{sessionId}")
    @SendTo("/topic/chat/{sessionId}")
    public ChatMessage sendMessage(
            @DestinationVariable String sessionId,
            @Payload ChatMessage chatMessage) {

        log.info("Received message for session {}: {}", sessionId, chatMessage.getContent());

        // Set session ID and timestamp
        chatMessage.setSessionId(sessionId);
        chatMessage.setTimestamp(LocalDateTime.now());

        // Save the message to MongoDB
        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

        // Update the last message timestamp in the session
        Optional<ChatSession> sessionOpt = chatSessionRepository.findById(sessionId);
        sessionOpt.ifPresent(session -> {
            session.setLastMessageAt(LocalDateTime.now());
            chatSessionRepository.save(session);
        });

        // Process message with bot assistant if enabled for this session
        if (sessionOpt.isPresent() && sessionOpt.get().isBotEnabled() && 
                chatMessage.getType() == ChatMessage.MessageType.CHAT) {
            botAssistantService.processMessage(savedMessage);
        }

        return savedMessage;
    }

    /**
     * Handle user join events.
     *
     * @param sessionId the session ID
     * @param chatMessage the join message
     * @param headerAccessor the message headers
     * @return the join message to be broadcast to subscribers
     */
    @MessageMapping("/chat/{sessionId}/join")
    @SendTo("/topic/chat/{sessionId}")
    public ChatMessage addUser(
            @DestinationVariable String sessionId,
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor) {

        log.info("User {} joined session {}", chatMessage.getSenderName(), sessionId);

        // Add username in WebSocket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSenderName());
        headerAccessor.getSessionAttributes().put("sessionId", sessionId);

        // Set message properties
        chatMessage.setSessionId(sessionId);
        chatMessage.setType(ChatMessage.MessageType.JOIN);
        chatMessage.setTimestamp(LocalDateTime.now());

        // Save the message
        return chatMessageRepository.save(chatMessage);
    }
}
