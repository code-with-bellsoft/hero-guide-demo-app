package com.github.asm0dey.chatapi.controller;

import com.github.asm0dey.chatapi.model.ChatSession;
import com.github.asm0dey.chatapi.model.User;
import com.github.asm0dey.chatapi.repository.ChatSessionRepository;
import com.github.asm0dey.chatapi.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Controller for web pages.
 */
@Controller
public class WebController {

    private final UserRepository userRepository;
    private final ChatSessionRepository chatSessionRepository;

    public WebController(UserRepository userRepository, ChatSessionRepository chatSessionRepository) {
        this.userRepository = userRepository;
        this.chatSessionRepository = chatSessionRepository;
    }

    /**
     * Renders the home page.
     *
     * @param model the model
     * @return the view name
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "Chat Application - Home");

        // Add authentication information to the model
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("authenticated", true);
            model.addAttribute("username", authentication.getName());
        } else {
            model.addAttribute("authenticated", false);
        }

        return "index";
    }

    /**
     * Renders the chat page.
     *
     * @param model the model
     * @return the view name
     */
    @GetMapping("/chat")
    public String chatView(Model model) {
        model.addAttribute("title", "Chat Application - Chat");

        // Add authentication information to the model
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            model.addAttribute("authenticated", true);
            model.addAttribute("username", username);

            // Find the user's ID
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isPresent()) {
                User user = userOpt.get();

                // Find the user's sessions
                List<ChatSession> sessions = chatSessionRepository.findByParticipant(user.getId());

                // If the user has sessions, use the first one's ID; otherwise, create a new session
                String sessionId;
                if (sessions.isEmpty()) {
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
                } else {
                    sessionId = sessions.getFirst().getId();
                }
                model.addAttribute("sessionId", sessionId);
            } else {
                // If the user is not found, use a default session ID
                model.addAttribute("sessionId", "default");
            }
        } else {
            model.addAttribute("authenticated", false);
            model.addAttribute("sessionId", "default");
        }

        return "chat-view";
    }

    /**
     * Renders the login page.
     *
     * @param error error message if authentication failed
     * @param model the model
     * @return the view name
     */
    @GetMapping("/login")
    public String loginView(@RequestParam(required = false) String error, Model model) {
        model.addAttribute("title", "Chat Application - Login");
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        return "login";
    }
}
