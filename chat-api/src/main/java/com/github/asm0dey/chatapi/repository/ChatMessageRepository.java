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
     * @param pageable  pagination information
     * @return a page of messages for the session
     */
    Page<ChatMessage> findBySessionId(String sessionId, Pageable pageable);

    /**
     * Find messages by session ID and timestamp after a given time.
     *
     * @param sessionId the session ID
     * @param timestamp the timestamp to search after
     * @return a list of messages for the session after the given timestamp
     */
    List<ChatMessage> findBySessionIdAndTimestampAfter(String sessionId, LocalDateTime timestamp);

    /**
     * Count messages by session ID.
     *
     * @param sessionId the session ID
     * @return the number of messages in the session
     */
    long countBySessionId(String sessionId);
}