/*
 * Copyright © 2025 BellSoft (info@bell-sw.com)
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
package com.github.asm0dey.chatapi.listener;

import com.github.asm0dey.chatapi.model.ChatMessage;
import com.github.asm0dey.chatapi.repository.ChatMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;

/**
 * Event listener for WebSocket connection events.
 * Handles user disconnections to notify other users when someone leaves a chat session.
 */
@Component
public class WebSocketEventListener {
    private static final Logger log = LoggerFactory.getLogger(WebSocketEventListener.class);

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;

    public WebSocketEventListener(SimpMessageSendingOperations messagingTemplate,
                                  ChatMessageRepository chatMessageRepository) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageRepository = chatMessageRepository;
    }

    /**
     * Handle WebSocket disconnect events.
     * When a user disconnects, send a leave message to the chat session.
     *
     * @param event the disconnect event
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // Get username and session ID from WebSocket session attributes
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String sessionId = (String) headerAccessor.getSessionAttributes().get("sessionId");

        if (username != null && sessionId != null) {
            log.info("User {} disconnected from session {}", username, sessionId);

            // Create leave message
            ChatMessage leaveMessage = new ChatMessage();
            leaveMessage.setType(ChatMessage.MessageType.LEAVE);
            leaveMessage.setSenderName(username);
            leaveMessage.setSessionId(sessionId);
            leaveMessage.setTimestamp(LocalDateTime.now());
            leaveMessage.setContent(username + " left the chat");

            // Save the leave message
            chatMessageRepository.save(leaveMessage);

            // Send leave message to the session
            messagingTemplate.convertAndSend("/topic/chat/" + sessionId, leaveMessage);
        }
    }
}
