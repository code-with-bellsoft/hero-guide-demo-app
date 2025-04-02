package com.github.asm0dey.chatapi.repository;

import com.github.asm0dey.chatapi.model.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for ChatMessage entity operations.
 */
@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    /**
     * Find messages by session ID.
     *
     * @param sessionId the session ID
     * @return a list of messages for the session
     */
    List<ChatMessage> findBySessionId(String sessionId);

    /**
     * Find messages by session ID with pagination.
     *
     * @param sessionId the session ID
     * @param pageable pagination information
     * @return a page of messages for the session
     */
    Page<ChatMessage> findBySessionId(String sessionId, Pageable pageable);

    /**
     * Find messages by sender ID.
     *
     * @param senderId the sender ID
     * @return a list of messages from the sender
     */
    List<ChatMessage> findBySenderId(String senderId);

    /**
     * Find messages by session ID and timestamp after a given time.
     *
     * @param sessionId the session ID
     * @param timestamp the timestamp to search after
     * @return a list of messages for the session after the given timestamp
     */
    List<ChatMessage> findBySessionIdAndTimestampAfter(String sessionId, LocalDateTime timestamp);

    /**
     * Find messages that need to be processed by the bot.
     *
     * @param processedByBot flag indicating if the message has been processed by the bot
     * @return a list of messages that need bot processing
     */
    List<ChatMessage> findByProcessedByBot(boolean processedByBot);

    /**
     * Count messages by session ID.
     *
     * @param sessionId the session ID
     * @return the number of messages in the session
     */
    long countBySessionId(String sessionId);
}