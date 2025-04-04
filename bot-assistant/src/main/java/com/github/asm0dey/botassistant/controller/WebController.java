package com.github.asm0dey.botassistant.controller;

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

import gg.jte.generated.precompiled.Templates;
import gg.jte.models.runtime.JteModel;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 * Controller for web pages.
 */
@RestController
public class WebController {

    private final Templates templates;

    public WebController(Templates templates) {
        this.templates = templates;
    }

    /**
     * Renders the home page.
     *
     * @return the view model
     */
    @GetMapping(value = "/", produces = TEXT_HTML_VALUE)
    public JteModel index() {
        return templates.index("Bot Assistant - Home");
    }

    /**
     * Renders the assistant page.
     *
     * @return the view model
     */
    @GetMapping(value = "/assistant", produces = TEXT_HTML_VALUE)
    public JteModel assistant() {
        return templates.assistantView("Bot Assistant - Chat");
    }

    /**
     * Renders the admin page.
     *
     * @return the view model
     */
    @GetMapping(value = "/admin", produces = TEXT_HTML_VALUE)
    public JteModel admin() {
        return templates.botAdmin("Bot Assistant - Admin");
    }
}
