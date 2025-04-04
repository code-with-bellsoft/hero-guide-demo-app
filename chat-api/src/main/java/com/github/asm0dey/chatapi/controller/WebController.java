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
import gg.jte.generated.precompiled.Templates;
import gg.jte.models.runtime.JteModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 * Controller for web pages.
 */
@RestController
public class WebController {

    private final UserRepository userRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final Templates templates;

    public WebController(UserRepository userRepository, ChatSessionRepository chatSessionRepository, Templates templates) {
        this.userRepository = userRepository;
        this.chatSessionRepository = chatSessionRepository;
        this.templates = templates;
    }

    @GetMapping(value = "/", produces = TEXT_HTML_VALUE)
    public JteModel index(Authentication authentication) {
        return templates.index("Chat Application - Home",
                isAuthenticated(authentication),
                authentication == null ? "" : authentication.getName(),
                isAdmin(authentication));
    }

    /**
     * Renders the chat page.
     *
     * @return the view name
     */
    @GetMapping(value = "/chat", produces = TEXT_HTML_VALUE)
    public JteModel chatView(Authentication authentication) {
        String username = authentication == null ? "" : authentication.getName();

        // Find the user's ID
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Find the user's sessions
            List<ChatSession> sessions = chatSessionRepository.findByParticipant(user.getId());

            // If the user has sessions, use the first one's ID; otherwise, create a new session
            String sessionId = sessions.isEmpty() ? createSession(user, username) : sessions.getFirst().getId();
            return templates.chatView("Chat Application - Chat",
                    true,
                    authentication == null ? "" : authentication.getName(),
                    sessionId,
                    isAdmin(authentication));
        } else {
            return templates.chatView("Chat Application - Chat",
                    isAuthenticated(authentication),
                    authentication == null ? "" : authentication.getName(),
                    "default",
                    isAdmin(authentication));
        }

    }

    private String createSession(User user, String username) {
        String sessionId;
        // Create a new session for the user
        LocalDateTime now = LocalDateTime.now();
        ChatSession newSession = ChatSession.builder()
                .id("session-" + user.getId() + "-" + System.currentTimeMillis())
                .name("Chat Session for " + username)
                .description("Automatically created chat session")
                .createdBy(user.getId())
                .participants(List.of(user.getId()))
                .isActive(true)
                .createdAt(now)
                .updatedAt(now)
                .lastMessageAt(now)
                .isPrivate(false)
                .botEnabled(true)
                .build();

        ChatSession savedSession = chatSessionRepository.save(newSession);
        sessionId = savedSession.getId();
        return sessionId;
    }

    /**
     * Renders the login page.
     *
     * @param error error message if authentication failed
     * @return the view name
     */
    @GetMapping(value = "/login", produces = TEXT_HTML_VALUE)
    public JteModel loginView(@RequestParam(required = false) String error) {
        return templates.login("Chat Application - Login",
                error != null ? "Invalid username or password" : null);
    }

    /**
     * Renders the admin sessions page.
     *
     * @return the view name
     */
    @GetMapping(value = "/admin/sessions", produces = TEXT_HTML_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public JteModel adminSessionsView(Authentication authentication) {
        return templates.sessionsAdmin("Admin - Sessions",
                isAuthenticated(authentication),
                authentication == null ? "" : authentication.getName(),
                isAdmin(authentication));
    }

    /**
     * Renders the admin chat history page.
     *
     * @param sessionId optional session ID to filter messages
     * @return the view name
     */
    @GetMapping(value = {"/admin/chat/history", "/admin/chat/history/{sessionId}"}, produces = TEXT_HTML_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public JteModel adminChatHistoryView(@PathVariable(required = false) String sessionId, Authentication authentication) {
        return templates.chatHistoryAdmin("Admin - Chat History",
                isAuthenticated(authentication),
                authentication == null ? "" : authentication.getName(),
                isAdmin(authentication),
                Objects.requireNonNullElse(sessionId, ""));
    }

    /**
     * Renders the admin users page.
     *
     * @return the view name
     */
    @GetMapping(value = "/admin/users", produces = TEXT_HTML_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public JteModel adminUsersView(Authentication authentication) {
        return templates.usersAdmin("Admin - Users",
                isAuthenticated(authentication),
                authentication == null ? "" : authentication.getName(),
                isAdmin(authentication));
    }

    /**
     * Check if the user is authenticated.
     *
     * @param authentication the authentication object
     * @return true if the user is authenticated, false otherwise
     */
    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser");
    }

    /**
     * Check if the user has the ADMIN role.
     *
     * @param authentication the authentication object
     * @return true if the user is an admin, false otherwise
     */
    private boolean isAdmin(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) return false;
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.toUpperCase().contains("ROLE_ADMIN"));
    }
}
