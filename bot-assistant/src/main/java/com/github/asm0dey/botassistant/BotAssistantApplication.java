package com.github.asm0dey.botassistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main application class for the Bot Assistant service.
 * This service provides AI-powered responses to chat messages
 * using OpenAI API and caches responses in Redis.
 */
@SpringBootApplication
@EnableCaching
@EnableAsync
public class BotAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(BotAssistantApplication.class, args);
    }
}