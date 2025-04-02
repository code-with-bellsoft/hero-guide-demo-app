package com.github.asm0dey.chatapi.service;

import com.github.asm0dey.chatapi.model.ChatMessage;
import com.github.asm0dey.chatapi.repository.ChatMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Implementation of the BotAssistantService that communicates with the Bot Assistant module.
 */
@Service
public class BotAssistantService {
    private static final Logger log = LoggerFactory.getLogger(BotAssistantService.class);

    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final WebClient.Builder webClientBuilder;

    @Value("${bot.assistant.url:http://localhost:8081}")
    private String botAssistantUrl;

    public BotAssistantService(ChatMessageRepository chatMessageRepository,
                               SimpMessagingTemplate messagingTemplate,
                               WebClient.Builder webClientBuilder) {
        this.chatMessageRepository = chatMessageRepository;
        this.messagingTemplate = messagingTemplate;
        this.webClientBuilder = webClientBuilder;
    }

    /**
     * Process a chat message with the bot assistant.
     * This method is executed asynchronously to avoid blocking the WebSocket thread.
     *
     * @param message the message to process
     */
    @Async
    public void processMessage(ChatMessage message) {
        log.info("Processing message with bot assistant: {}", message.getContent());
        log.debug("Bot assistant URL: {}", botAssistantUrl);
        log.debug("Message details: id={}, sessionId={}, type={}, sender={}", 
                message.getId(), message.getSessionId(), message.getType(), message.getSenderName());

        // Mark the message as being processed by the bot
        message.setProcessedByBot(true);
        chatMessageRepository.save(message);

        // Create a request to the bot assistant
        WebClient webClient = webClientBuilder.baseUrl(botAssistantUrl).build();
        log.debug("Sending request to bot assistant at {}/api/bot/process", botAssistantUrl);

        webClient.post()
                .uri("/api/bot/process")
                .bodyValue(message)
                .retrieve()
                .bodyToMono(ChatMessage.class)
                .timeout(Duration.ofSeconds(10))
                .doOnSuccess(response -> {
                    log.debug("Received successful response from bot assistant: {}", response);
                })
                .onErrorResume(e -> {
                    log.error("Error processing message with bot assistant", e);
                    // Create a fallback message in case of error
                    ChatMessage botMessage = new ChatMessage();
                    botMessage.setSessionId(message.getSessionId());
                    botMessage.setType(ChatMessage.MessageType.BOT);
                    botMessage.setSenderId("bot");
                    botMessage.setSenderName("Bot Assistant");
                    botMessage.setContent("Sorry, I'm currently unavailable. Please try again later.");
                    botMessage.setTimestamp(LocalDateTime.now());
                    return Mono.just(botMessage);
                })
                .subscribe(botResponse -> {
                    log.debug("Processing bot response: {}", botResponse);

                    // Save the bot response
                    ChatMessage savedBotResponse = chatMessageRepository.save(botResponse);
                    log.debug("Saved bot response: {}", savedBotResponse);

                    // Send the bot response to the WebSocket topic
                    String destination = "/topic/chat/" + message.getSessionId();
                    log.debug("Sending bot response to WebSocket topic: {}", destination);

                    try {
                        messagingTemplate.convertAndSend(destination, savedBotResponse);
                        log.info("Bot response sent for message: {}", message.getId());
                    } catch (Exception e) {
                        log.error("Error sending bot response to WebSocket topic", e);
                    }
                });
    }
}
