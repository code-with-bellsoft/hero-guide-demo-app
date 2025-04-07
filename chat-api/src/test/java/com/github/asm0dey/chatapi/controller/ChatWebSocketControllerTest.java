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
package com.github.asm0dey.chatapi.controller;

import com.github.asm0dey.chatapi.model.ChatMessage;
import com.github.asm0dey.chatapi.model.ChatSession;
import com.github.asm0dey.chatapi.repository.ChatMessageRepository;
import com.github.asm0dey.chatapi.repository.ChatSessionRepository;
import com.github.asm0dey.chatapi.service.BotAssistantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class ChatWebSocketControllerTest {

    @Container
    @ServiceConnection
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");
    private final String username = "testUser";
    @LocalServerPort
    private int port;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private ChatSessionRepository chatSessionRepository;
    @Autowired
    private org.springframework.messaging.simp.SimpMessagingTemplate messagingTemplate;
    @MockitoBean
    private BotAssistantService botAssistantService;
    private WebSocketStompClient stompClient;
    private String sessionId;

    @BeforeEach
    public void setup() {
        // Clear repositories
        chatMessageRepository.deleteAll();
        chatSessionRepository.deleteAll();

        // Create a test session with bot enabled
        sessionId = "test-session-" + System.currentTimeMillis();
        ChatSession chatSession = ChatSession.builder()
                .id(sessionId)
                .name("Test Session")
                .createdBy("testUser")
                .participants(List.of("testUser"))
                .isActive(true)
                .botEnabled(true)
                .createdAt(LocalDateTime.now())
                .build();
        chatSessionRepository.save(chatSession);

        // Set up WebSocket client
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        SockJsClient sockJsClient = new SockJsClient(transports);

        stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        // Configure BotAssistantService mock to simulate bot response
        doAnswer(invocation -> {
            ChatMessage message = invocation.getArgument(0);
            // Simulate async processing by the bot
            CompletableFuture.runAsync(() -> {
                try {
                    // Wait a bit to simulate processing time
                    Thread.sleep(500);

                    // Create a bot response
                    ChatMessage botResponse = new ChatMessage();
                    botResponse.setSessionId(message.getSessionId());
                    botResponse.setType(ChatMessage.MessageType.BOT);
                    botResponse.setSenderId("bot");
                    botResponse.setSenderName("Bot Assistant");
                    botResponse.setContent("This is a test bot response to: " + message.getContent());
                    botResponse.setTimestamp(LocalDateTime.now());

                    // Save the bot response
                    ChatMessage savedBotResponse = chatMessageRepository.save(botResponse);

                    // Send the bot response to the WebSocket topic
                    String destination = "/topic/chat/" + message.getSessionId();
                    System.out.println("[DEBUG_LOG] Sending bot response to: " + destination);
                    messagingTemplate.convertAndSend(destination, savedBotResponse);
                    System.out.println("[DEBUG_LOG] Bot response sent: " + savedBotResponse);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            return null;
        }).when(botAssistantService).processMessage(any(ChatMessage.class));
    }

    @Test
    public void testSendMessage() throws ExecutionException, InterruptedException, TimeoutException {
        System.out.println("[DEBUG_LOG] Starting WebSocket test");

        // Connect to WebSocket
        String wsUrl = "ws://localhost:" + port + "/ws";
        StompSession session = stompClient.connectAsync(wsUrl, new StompSessionHandlerAdapter() {
        }).get(5, TimeUnit.SECONDS);
        System.out.println("[DEBUG_LOG] Connected to WebSocket at " + wsUrl);

        // Subscribe to the topic (even though we won't receive messages in the test)
        session.subscribe("/topic/chat/" + sessionId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("[DEBUG_LOG] Received message: " + payload);
            }
        });
        System.out.println("[DEBUG_LOG] Subscribed to /topic/chat/" + sessionId);

        // Send a message
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSessionId(sessionId);
        chatMessage.setType(ChatMessage.MessageType.CHAT);
        chatMessage.setSenderId(username);
        chatMessage.setSenderName(username);
        chatMessage.setContent("Hello, this is a test message!");

        System.out.println("[DEBUG_LOG] Sending message: " + chatMessage);
        session.send("/app/chat/" + sessionId, chatMessage);

        // Wait a bit for the message to be processed and the bot to respond
        Thread.sleep(2000);

        // Verify that the messages were saved to the repository
        List<ChatMessage> messages = chatMessageRepository.findBySessionId(sessionId);
        System.out.println("[DEBUG_LOG] Messages in repository: " + messages);

        // We should have at least 2 messages: the user message and the bot response
        assertThat(messages).hasSizeGreaterThanOrEqualTo(2);

        // Find the user message and bot response in the repository
        ChatMessage userMessage = messages.stream()
                .filter(m -> m.getType() == ChatMessage.MessageType.CHAT)
                .findFirst()
                .orElseThrow(() -> new AssertionError("User message not found in repository"));

        ChatMessage botResponse = messages.stream()
                .filter(m -> m.getType() == ChatMessage.MessageType.BOT)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Bot response not found in repository"));

        // Verify the user message was saved correctly
        assertThat(userMessage.getContent()).isEqualTo("Hello, this is a test message!");
        assertThat(userMessage.getSenderName()).isEqualTo(username);

        // Verify the bot response was saved correctly
        assertThat(botResponse.getSenderName()).isEqualTo("Bot Assistant");
        assertThat(botResponse.getContent()).contains("This is a test bot response to:");
        assertThat(botResponse.getContent()).contains("Hello, this is a test message!");

        // Verify that the bot assistant service was called
        verify(botAssistantService, timeout(1000).times(1)).processMessage(any(ChatMessage.class));

        // Capture the argument passed to processMessage
        ArgumentCaptor<ChatMessage> messageCaptor = ArgumentCaptor.forClass(ChatMessage.class);
        verify(botAssistantService).processMessage(messageCaptor.capture());
        ChatMessage capturedMessage = messageCaptor.getValue();

        // Verify the correct message was passed to the bot assistant
        assertThat(capturedMessage.getContent()).isEqualTo("Hello, this is a test message!");
        assertThat(capturedMessage.getSessionId()).isEqualTo(sessionId);
    }
}
