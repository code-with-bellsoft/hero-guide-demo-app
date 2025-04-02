package com.github.asm0dey.chatapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for web pages.
 */
@Controller
public class WebController {

    /**
     * Renders the home page.
     *
     * @param model the model
     * @return the view name
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "Chat Application - Home");
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
        return "chat-view";
    }
}
