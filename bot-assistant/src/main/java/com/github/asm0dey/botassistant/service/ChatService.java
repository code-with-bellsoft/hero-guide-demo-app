package com.github.asm0dey.botassistant.service;

import com.github.asm0dey.botassistant.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation of the ChatService using WebClient to call OpenAI API.
 */
@Service
public class ChatService {
    private static final Logger log = LoggerFactory.getLogger(ChatService.class);

    private final WebClient.Builder webClientBuilder;
    private final CacheService cacheService;

    public ChatService(WebClient.Builder webClientBuilder, CacheService cacheService) {
        this.webClientBuilder = webClientBuilder;
        this.cacheService = cacheService;
    }

    @Value("${spring.ai.openai.api-key}")
    private String openaiApiKey;

    @Value("${spring.ai.openai.base-url:https://api.openai.com}")
    private String openaiBaseUrl;

    @Value("${spring.ai.openai.model:gpt-3.5-turbo}")
    private String model;

    @Value("${bot.system-prompt:You are a helpful assistant that provides concise and accurate information.}")
    private String systemPrompt;

    private final AtomicLong totalRequests = new AtomicLong(0);
    private final AtomicLong aiRequests = new AtomicLong(0);
    private final AtomicLong errorCount = new AtomicLong(0);
    private final AtomicLong totalTokens = new AtomicLong(0);

    // List of preprogrammed answers to use when OpenAI API key is not available
    private final List<String> preprogrammedAnswers = List.of(
        "I'm currently operating in offline mode. Could you try again later when I'm back online?",
        "I'm sorry, but I'm currently unable to access my full capabilities. I'll be back to normal soon!",
        "I'm in energy-saving mode right now. Please check back later when I'm fully powered up.",
        "My connection to the cloud is temporarily unavailable. I can only provide basic responses at the moment.",
        "I'm currently running on my backup system. I'll have my full intelligence restored shortly.",
        "I'm operating with limited functionality right now. Please try again later for a more comprehensive response.",
        "I'm currently in maintenance mode. I'll be back with my full capabilities soon!",
        "I can only provide simple responses right now as I'm temporarily disconnected from my knowledge base.",
        "I'm working with reduced capabilities at the moment. Please check back later when I'm fully operational.",
        "My advanced reasoning module is currently offline. I'll be back to normal operations soon!"
    );

    private final Random random = new Random();

    /**
     * Process a chat message and generate a response.
     *
     * @param message the message to process
     * @return the generated response
     */
    @SuppressWarnings("unchecked")
    public ChatMessage processMessage(ChatMessage message) {
        log.info("Processing message: {}", message.content());
        totalRequests.incrementAndGet();

        // Check cache first
        Optional<ChatMessage> cachedResponse = cacheService.getCachedResponse(message);
        if (cachedResponse.isPresent()) {
            log.info("Using cached response for message: {}", message.content());
            return cachedResponse.get();
        }

        // Check if OpenAI API key is available
        if (openaiApiKey == null || openaiApiKey.isEmpty() || openaiApiKey.equals("your-api-key-here")) {
            log.warn("OpenAI API key is not available. Using preprogrammed answer.");
            errorCount.incrementAndGet();
            return createBotResponse(message, getRandomPreprogrammedAnswer());
        }

        // Generate response using OpenAI API
        try {
            aiRequests.incrementAndGet();

            // Create request body for OpenAI API
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);

            // Create messages array
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", systemPrompt);

            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", message.content());

            requestBody.put("messages", List.of(systemMessage, userMessage));

            // Call OpenAI API
            WebClient client = webClientBuilder
                    .baseUrl(openaiBaseUrl)
                    .defaultHeader("Authorization", "Bearer " + openaiApiKey)
                    .build();

            Map<String, Object> response = client.post()
                    .uri("/v1/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            // Extract response content
            String content = extractContentFromResponse(response);

            // Update token count if available
            if (Objects.requireNonNull(response).containsKey("usage")) {
                Map<String, Object> usage = (Map<String, Object>) response.get("usage");
                if (usage.containsKey("total_tokens")) {
                    totalTokens.addAndGet(((Number) usage.get("total_tokens")).longValue());
                }
            }

            // Create bot response
            ChatMessage botResponse = createBotResponse(message, content);

            // Cache the response
            cacheService.cacheResponse(message, botResponse);

            log.info("Generated AI response for message: {}", message.content());
            return botResponse;
        } catch (WebClientResponseException.Unauthorized e) {
            // This exception is thrown when the API key is invalid
            log.error("Unauthorized: Invalid OpenAI API key", e);
            errorCount.incrementAndGet();
            return createBotResponse(message, getRandomPreprogrammedAnswer());
        } catch (Exception e) {
            log.error("Error generating AI response", e);
            errorCount.incrementAndGet();

            // Create fallback response
            return createBotResponse(message, "I'm sorry, I'm having trouble processing your request right now. Please try again later.");
        }
    }

    /**
     * Extract content from OpenAI API response.
     *
     * @param response the OpenAI API response
     * @return the extracted content
     */
    private String extractContentFromResponse(Map<String, Object> response) {
        try {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> choice = choices.get(0);
                Map<String, Object> message = (Map<String, Object>) choice.get("message");
                return (String) message.get("content");
            }
        } catch (Exception e) {
            log.error("Error extracting content from response", e);
        }
        return "I'm sorry, I couldn't generate a proper response.";
    }

    /**
     * Get statistics about the chat service.
     *
     * @return a string containing statistics
     */
    public String getStatistics() {
        long total = totalRequests.get();
        long ai = aiRequests.get();
        long cached = total - ai;
        double cacheHitRatio = cacheService.getCacheHitRatio();
        long errors = errorCount.get();
        long tokens = totalTokens.get();

        return String.format(
                "Total requests: %d\n" +
                        "AI requests: %d\n" +
                        "Cached responses: %d\n" +
                        "Cache hit ratio: %.2f\n" +
                        "Errors: %d\n" +
                        "Total tokens used: %d",
                total, ai, cached, cacheHitRatio, errors, tokens);
    }

    /**
     * Create a bot response message.
     *
     * @param originalMessage the original message
     * @param content         the response content
     * @return the bot response message
     */
    private ChatMessage createBotResponse(ChatMessage originalMessage, String content) {
        return new ChatMessage(
                null,                       // id (will be generated)
                originalMessage.sessionId(),    // sessionId
                "bot",                          // senderId
                "Bot Assistant",                // senderName
                ChatMessage.MessageType.BOT,    // type
                content,                        // content
                LocalDateTime.now(),            // timestamp
                false                           // processedByBot
        );
    }

    /**
     * Get a random preprogrammed answer.
     *
     * @return a random preprogrammed answer
     */
    private String getRandomPreprogrammedAnswer() {
        int index = random.nextInt(preprogrammedAnswers.size());
        return preprogrammedAnswers.get(index);
    }
}
