package com.github.asm0dey.chatapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main application class for the Chat API service.
 * This service provides WebSocket endpoints for real-time chat
 * and REST endpoints for chat history and user management.
 */
@SpringBootApplication
@EnableMongoRepositories
@EnableAsync
public class ChatApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatApiApplication.class, args);
    }
}
