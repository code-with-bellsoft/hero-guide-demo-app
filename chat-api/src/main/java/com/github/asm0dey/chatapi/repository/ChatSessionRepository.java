package com.github.asm0dey.chatapi.repository;

import com.github.asm0dey.chatapi.model.ChatSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for ChatSession entity operations.
 */
@Repository
public interface ChatSessionRepository extends MongoRepository<ChatSession, String> {

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
     * @param userId   the user ID
     * @param pageable pagination information
     * @return a page of sessions where the user is a participant
     */
    @Query("{ 'participants': ?0 }")
    Page<ChatSession> findByParticipant(String userId, Pageable pageable);

}