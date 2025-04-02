package com.github.asm0dey.chatapi.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a chat session between users.
 */
@Document(collection = "sessions")
public class ChatSession {

    @Id
    private String id;

    private String name;

    private String description;

    @Indexed
    private String createdBy;

    private List<String> participants;

    private boolean isActive;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime lastMessageAt;

    /**
     * Indicates if this is a private chat between two users.
     */
    private boolean isPrivate;

    /**
     * Indicates if the bot assistant is enabled for this session.
     */
    private boolean botEnabled;

    /**
     * Constructor with all fields.
     */
    public ChatSession(String id, String name, String description, String createdBy,
                       List<String> participants, boolean isActive, LocalDateTime createdAt,
                       LocalDateTime updatedAt, LocalDateTime lastMessageAt, boolean isPrivate,
                       boolean botEnabled) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
        this.participants = participants != null ? participants : new ArrayList<>();
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastMessageAt = lastMessageAt;
        this.isPrivate = isPrivate;
        this.botEnabled = botEnabled;
    }

    /**
     * Create a new builder instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getLastMessageAt() {
        return lastMessageAt;
    }

    public void setLastMessageAt(LocalDateTime lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public boolean isBotEnabled() {
        return botEnabled;
    }

    public void setBotEnabled(boolean botEnabled) {
        this.botEnabled = botEnabled;
    }

    /**
     * Builder for ChatSession.
     */
    public static class Builder {
        private String id;
        private String name;
        private String description;
        private String createdBy;
        private List<String> participants = new ArrayList<>();
        private boolean isActive = true;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime lastMessageAt;
        private boolean isPrivate = false;
        private boolean botEnabled = true;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder participants(List<String> participants) {
            this.participants = participants;
            return this;
        }

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder lastMessageAt(LocalDateTime lastMessageAt) {
            this.lastMessageAt = lastMessageAt;
            return this;
        }

        public Builder isPrivate(boolean isPrivate) {
            this.isPrivate = isPrivate;
            return this;
        }

        public Builder botEnabled(boolean botEnabled) {
            this.botEnabled = botEnabled;
            return this;
        }

        public ChatSession build() {
            return new ChatSession(id, name, description, createdBy, participants, isActive,
                    createdAt, updatedAt, lastMessageAt, isPrivate, botEnabled);
        }
    }
}
