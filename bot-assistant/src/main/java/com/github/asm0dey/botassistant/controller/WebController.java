package com.github.asm0dey.botassistant.controller;

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
        model.addAttribute("title", "Bot Assistant - Home");
        return "index";
    }

    /**
     * Renders the assistant page.
     *
     * @param model the model
     * @return the view name
     */
    @GetMapping("/assistant")
    public String assistant(Model model) {
        model.addAttribute("title", "Bot Assistant - Chat");
        return "assistant-view";
    }
}