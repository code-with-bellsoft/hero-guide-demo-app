package com.github.asm0dey.chatapi.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a chat message in the system.
 */
@Document(collection = "messages")
public class ChatMessage {

    @Id
    private String id;

    @Indexed
    private String sessionId;

    @Indexed
    private String senderId;

    private String senderName;

    private MessageType type;

    private String content;

    @CreatedDate
    private LocalDateTime timestamp;

    /**
     * Indicates if the message was processed by the bot assistant.
     */
    private boolean processedByBot = false;

    /**
     * Default constructor required by MongoDB.
     */
    public ChatMessage() {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isProcessedByBot() {
        return processedByBot;
    }

    public void setProcessedByBot(boolean processedByBot) {
        this.processedByBot = processedByBot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessage that = (ChatMessage) o;
        return processedByBot == that.processedByBot &&
                Objects.equals(id, that.id) &&
                Objects.equals(sessionId, that.sessionId) &&
                Objects.equals(senderId, that.senderId) &&
                Objects.equals(senderName, that.senderName) &&
                type == that.type &&
                Objects.equals(content, that.content) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sessionId, senderId, senderName, type, content, timestamp, processedByBot);
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "id='" + id + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", senderId='" + senderId + '\'' +
                ", senderName='" + senderName + '\'' +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                ", processedByBot=" + processedByBot +
                '}';
    }

    /**
     * Types of messages that can be exchanged.
     */
    public enum MessageType {
        CHAT,       // Regular chat message
        JOIN,       // User joined notification
        LEAVE,      // User left notification
        BOT         // Message from bot assistant
    }

}
