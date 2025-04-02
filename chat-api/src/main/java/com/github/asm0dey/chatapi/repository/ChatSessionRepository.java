package com.github.asm0dey.chatapi.repository;

import com.github.asm0dey.chatapi.model.ChatSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for ChatSession entity operations.
 */
@Repository
public interface ChatSessionRepository extends MongoRepository<ChatSession, String> {

    /**
     * Find sessions created by a specific user.
     *
     * @param userId the user ID
     * @return a list of sessions created by the user
     */
    List<ChatSession> findByCreatedBy(String userId);

    /**
     * Find sessions created by a specific user with pagination.
     *
     * @param userId the user ID
     * @param pageable pagination information
     * @return a page of sessions created by the user
     */
    Page<ChatSession> findByCreatedBy(String userId, Pageable pageable);

    /**
     * Find active sessions.
     *
     * @return a list of active sessions
     */
    List<ChatSession> findByIsActiveTrue();

    /**
     * Find sessions where a user is a participant.
     *
     * @param userId the user ID
     * @return a list of sessions where the user is a participant
     */
    @Query("{ 'participants': ?0 }")
    List<ChatSession> findByParticipant(String userId);

    /**
     * Find sessions where a user is a participant with pagination.
     *
     * @param userId the user ID
     * @param pageable pagination information
     * @return a page of sessions where the user is a participant
     */
    @Query("{ 'participants': ?0 }")
    Page<ChatSession> findByParticipant(String userId, Pageable pageable);

    /**
     * Find private sessions between two users.
     *
     * @param userId1 the first user ID
     * @param userId2 the second user ID
     * @return a list of private sessions between the two users
     */
    @Query("{ 'isPrivate': true, 'participants': { $all: [?0, ?1] }, 'participants': { $size: 2 } }")
    List<ChatSession> findPrivateSessionBetweenUsers(String userId1, String userId2);

    /**
     * Find sessions with bot assistant enabled.
     *
     * @return a list of sessions with bot assistant enabled
     */
    List<ChatSession> findByBotEnabledTrue();

    /**
     * Find sessions updated after a specific time.
     *
     * @param dateTime the time to search after
     * @return a list of sessions updated after the specified time
     */
    List<ChatSession> findByUpdatedAtAfter(LocalDateTime dateTime);
}