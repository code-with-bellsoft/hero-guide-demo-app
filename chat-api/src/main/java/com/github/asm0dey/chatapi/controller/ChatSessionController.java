package com.github.asm0dey.chatapi.controller;

/*-
 * #%L
 * hero-guide-demo-app
 * %%
 * Copyright (C) 2025 BellSoft
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.github.asm0dey.chatapi.model.ChatSession;
import com.github.asm0dey.chatapi.model.User;
import com.github.asm0dey.chatapi.repository.ChatSessionRepository;
import com.github.asm0dey.chatapi.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing chat sessions.
 */
@RestController
@RequestMapping("/api/sessions")
public class ChatSessionController {

    private final ChatSessionRepository chatSessionRepository;
    private final UserRepository userRepository;

    public ChatSessionController(ChatSessionRepository chatSessionRepository, UserRepository userRepository) {
        this.chatSessionRepository = chatSessionRepository;
        this.userRepository = userRepository;
    }

    /**
     * Get all chat sessions for the authenticated user.
     *
     * @return a list of chat sessions
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ChatSession>> getUserSessions() {
        String userId = getCurrentUserId();
        List<ChatSession> sessions = chatSessionRepository.findByParticipant(userId);
        return ResponseEntity.ok(sessions);
    }

    /**
     * Get all chat sessions (admin only).
     *
     * @return a list of all chat sessions
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ChatSession>> getAllSessions() {
        List<ChatSession> sessions = chatSessionRepository.findAll();
        return ResponseEntity.ok(sessions);
    }

    /**
     * Get paginated chat sessions for the authenticated user.
     *
     * @param page the page number (0-based)
     * @param size the page size
     * @return a page of chat sessions
     */
    @GetMapping("/page")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<ChatSession>> getUserSessionsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        String userId = getCurrentUserId();
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "lastMessageAt"));
        Page<ChatSession> sessions = chatSessionRepository.findByParticipant(userId, pageRequest);
        return ResponseEntity.ok(sessions);
    }

    /**
     * Get a specific chat session.
     *
     * @param id the session ID
     * @return the chat session
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ChatSession> getSessionById(@PathVariable String id) {
        String userId = getCurrentUserId();
        ChatSession session = chatSessionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));

        // Check if user is a participant
        if (!session.getParticipants().contains(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a participant in this session");
        }

        return ResponseEntity.ok(session);
    }

    /**
     * Create a new chat session.
     *
     * @param session the session to create
     * @return the created session
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ChatSession> createSession(@RequestBody ChatSession session) {
        String userId = getCurrentUserId();

        // Set creator
        session.setCreatedBy(userId);

        // Add creator to participants if not already included
        if (!session.getParticipants().contains(userId)) {
            session.getParticipants().add(userId);
        }

        // Set creation time
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());

        ChatSession savedSession = chatSessionRepository.save(session);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSession);
    }

    /**
     * Update a chat session.
     *
     * @param id             the session ID
     * @param sessionDetails the updated session details
     * @return the updated session
     */
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ChatSession> updateSession(
            @PathVariable String id,
            @RequestBody ChatSession sessionDetails) {

        String userId = getCurrentUserId();
        ChatSession session = chatSessionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));

        // Only creator or admin can update session
        if (!session.getCreatedBy().equals(userId) && isNotAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the creator can update this session");
        }

        // Update fields
        if (sessionDetails.getName() != null) {
            session.setName(sessionDetails.getName());
        }
        if (sessionDetails.getDescription() != null) {
            session.setDescription(sessionDetails.getDescription());
        }
        if (sessionDetails.getParticipants() != null && !sessionDetails.getParticipants().isEmpty()) {
            session.setParticipants(sessionDetails.getParticipants());
        }
        if (sessionDetails.isActive() != session.isActive()) {
            session.setActive(sessionDetails.isActive());
        }
        if (sessionDetails.isBotEnabled() != session.isBotEnabled()) {
            session.setBotEnabled(sessionDetails.isBotEnabled());
        }

        session.setUpdatedAt(LocalDateTime.now());

        ChatSession updatedSession = chatSessionRepository.save(session);
        return ResponseEntity.ok(updatedSession);
    }

    /**
     * Add a participant to a chat session.
     *
     * @param id       the session ID
     * @param username the username of the participant to add
     * @return the updated session
     */
    @PostMapping("/{id}/participants")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ChatSession> addParticipant(
            @PathVariable String id,
            @RequestParam String username) {

        String userId = getCurrentUserId();
        ChatSession session = chatSessionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));

        // Only creator or admin can add participants
        if (!session.getCreatedBy().equals(userId) && isNotAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the creator can add participants");
        }

        // Find user by username
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        // Add participant if not already in the session
        String participantId = userOpt.get().getId();
        if (!session.getParticipants().contains(participantId)) {
            session.getParticipants().add(participantId);
            session.setUpdatedAt(LocalDateTime.now());
            chatSessionRepository.save(session);
        }

        return ResponseEntity.ok(session);
    }

    /**
     * Remove a participant from a chat session.
     *
     * @param id     the session ID
     * @param userId the user ID of the participant to remove
     * @return the updated session
     */
    @DeleteMapping("/{id}/participants/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ChatSession> removeParticipant(
            @PathVariable String id,
            @PathVariable String userId) {

        String currentUserId = getCurrentUserId();
        ChatSession session = chatSessionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));

        // Only creator, admin, or the participant themselves can remove a participant
        if (!session.getCreatedBy().equals(currentUserId) && !currentUserId.equals(userId) && isNotAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to remove this participant");
        }

        // Remove participant
        if (session.getParticipants().contains(userId)) {
            session.getParticipants().remove(userId);
            session.setUpdatedAt(LocalDateTime.now());
            chatSessionRepository.save(session);
        }

        return ResponseEntity.ok(session);
    }

    /**
     * Delete a chat session.
     *
     * @param id the session ID
     * @return no content
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteSession(@PathVariable String id) {
        String userId = getCurrentUserId();
        ChatSession session = chatSessionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));

        // Only creator or admin can delete session
        if (!session.getCreatedBy().equals(userId) && isNotAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the creator can delete this session");
        }

        chatSessionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get the current user ID from the security context.
     *
     * @return the current user ID
     */
    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"))
                .getId();
    }

    /**
     * Check if the current user has the ADMIN role.
     *
     * @return true if the user is an admin, false otherwise
     */
    private boolean isNotAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
