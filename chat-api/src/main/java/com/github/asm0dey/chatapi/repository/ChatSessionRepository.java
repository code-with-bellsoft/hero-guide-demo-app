/*
 * Copyright © 2025 BellSoft (info@bell-sw.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
