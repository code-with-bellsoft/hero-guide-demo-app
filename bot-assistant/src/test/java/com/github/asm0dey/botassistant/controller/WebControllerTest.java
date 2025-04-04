/*
 * Copyright 2025 BellSoft (info@bell-sw.com)
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

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for WebController.
 * These tests use MockMvc to test the controller's integration with Spring MVC.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class WebControllerTest {
    @Container
    @ServiceConnection
    static RedisContainer redis = new RedisContainer("redis");

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test the index endpoint.
     */
    @Test
    public void testIndexEndpoint() throws Exception {
        MvcResult result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
                .andExpect(content().string(containsString("Bot Assistant - Home")))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("Bot Assistant"), "Response should contain application name");
        // Check for common elements on the home page
        assertTrue(content.contains("Welcome") || content.contains("welcome") || 
                   content.contains("Home") || content.contains("home"), 
                   "Response should contain welcome message or home page elements");
    }

    /**
     * Test the assistant endpoint.
     */
    @Test
    public void testAssistantEndpoint() throws Exception {
        MvcResult result = mockMvc.perform(get("/assistant"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
                .andExpect(content().string(containsString("Bot Assistant - Chat")))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("Bot Assistant"), "Response should contain application name");
        // Check for chat-related elements
        assertTrue(content.contains("Chat") || content.contains("chat") || 
                   content.contains("Message") || content.contains("message"), 
                   "Response should contain chat-related elements");
    }

    /**
     * Test the admin endpoint.
     */
    @Test
    public void testAdminEndpoint() throws Exception {
        MvcResult result = mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
                .andExpect(content().string(containsString("Bot Assistant - Admin")))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("Bot Assistant"), "Response should contain application name");
        // Check for admin-related elements
        assertTrue(content.contains("Admin") || content.contains("admin") || 
                   content.contains("Configuration") || content.contains("configuration"), 
                   "Response should contain admin-related elements");
    }

    /**
     * Test that webjars content is accessible.
     * This test verifies that the Bulma CSS file can be accessed via the webjars path.
     */
    @Test
    public void testWebjarsContent() throws Exception {
        mockMvc.perform(get("/webjars/bulma/1.0.3/css/bulma.min.css"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/css")));
    }
}
