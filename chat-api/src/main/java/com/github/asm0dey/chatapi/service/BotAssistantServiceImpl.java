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
public class BotAssistantServiceImpl implements BotAssistantService {
    private static final Logger log = LoggerFactory.getLogger(BotAssistantServiceImpl.class);

    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final WebClient.Builder webClientBuilder;

    @Value("${bot.assistant.url:http://localhost:8081}")
    private String botAssistantUrl;

    public BotAssistantServiceImpl(ChatMessageRepository chatMessageRepository,
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
    @Override
    @Async
    public void processMessage(ChatMessage message) {
        log.info("Processing message with bot assistant: {}", message.getContent());

        // Mark the message as being processed by the bot
        message.setProcessedByBot(true);
        chatMessageRepository.save(message);

        // Create a request to the bot assistant
        WebClient webClient = webClientBuilder.baseUrl(botAssistantUrl).build();

        webClient.post()
                .uri("/api/bot/process")
                .bodyValue(message)
                .retrieve()
                .bodyToMono(ChatMessage.class)
                .timeout(Duration.ofSeconds(10))
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
                    // Save the bot response
                    ChatMessage savedBotResponse = chatMessageRepository.save(botResponse);

                    // Send the bot response to the WebSocket topic
                    messagingTemplate.convertAndSend(
                            "/topic/chat/" + message.getSessionId(),
                            savedBotResponse);

                    log.info("Bot response sent for message: {}", message.getId());
                });
    }
}
