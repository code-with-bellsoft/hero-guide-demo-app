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
package com.github.asm0dey.chatapi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class WebControllerTest {
    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo");

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test the index endpoint as an anonymous user.
     */
    @Test
    public void testIndexEndpointAnonymous() throws Exception {
        MvcResult result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TEXT_HTML))
                .andExpect(content().string(containsString("Chat Application - Home")))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("Chat Application"), "Response should contain application name");
        // Anonymous user should see login option
        assertTrue(content.contains("Login") || content.contains("login"), "Anonymous user should see login option");
    }

    /**
     * Test the index endpoint as a regular user.
     */
    @Test
    @WithUserDetails("user1")
    public void testIndexEndpointAsUser() throws Exception {
        MvcResult result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TEXT_HTML))
                .andExpect(content().string(containsString("Chat Application - Home")))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("user1"), "Response should contain username");
        // Regular user should not see admin options
        assertTrue(!content.contains("Admin Dashboard") || !content.contains("admin dashboard"), 
                "Regular user should not see admin options");
    }

    /**
     * Test the index endpoint as an admin user.
     */
    @Test
    @WithUserDetails("admin1")
    public void testIndexEndpointAsAdmin() throws Exception {
        MvcResult result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TEXT_HTML))
                .andExpect(content().string(containsString("Chat Application - Home")))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("admin1"), "Response should contain username");
        // Admin user should see admin options
        assertTrue(content.contains("Admin") || content.contains("admin"), 
                "Admin user should see admin options");
    }

    /**
     * Test the chat endpoint when authenticated as a regular user.
     */
    @Test
    @WithUserDetails("user1")
    public void testChatEndpointAsUser() throws Exception {
        MvcResult result = mockMvc.perform(get("/chat"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TEXT_HTML))
                .andExpect(content().string(containsString("Chat Application - Chat")))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("user1"), "Response should contain username");
        // Should contain chat elements
        assertTrue(content.contains("message") || content.contains("chat"), 
                "Response should contain chat elements");
    }

    /**
     * Test the chat endpoint when authenticated as an admin user.
     */
    @Test
    @WithUserDetails("admin1")
    public void testChatEndpointAsAdmin() throws Exception {
        MvcResult result = mockMvc.perform(get("/chat"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TEXT_HTML))
                .andExpect(content().string(containsString("Chat Application - Chat")))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("admin1"), "Response should contain username");
        // Admin user should see admin options
        assertTrue(content.contains("Admin") || content.contains("admin"), 
                "Admin user should see admin options");
    }

    /**
     * Test the chat endpoint redirects when not authenticated.
     */
    @Test
    public void testChatEndpointRedirects() throws Exception {
        mockMvc.perform(get("/chat"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    /**
     * Test the login endpoint.
     */
    @Test
    public void testLoginEndpoint() throws Exception {
        MvcResult result = mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TEXT_HTML))
                .andExpect(content().string(containsString("Chat Application - Login")))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("Login") || content.contains("login"), 
                "Response should contain login form");
        assertTrue(content.contains("Username") || content.contains("username"), 
                "Response should contain username field");
        assertTrue(content.contains("Password") || content.contains("password"), 
                "Response should contain password field");
    }

    /**
     * Test the admin sessions endpoint as an admin user.
     */
    @Test
    @WithUserDetails("admin1")
    public void testAdminSessionsEndpoint() throws Exception {
        MvcResult result = mockMvc.perform(get("/admin/sessions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TEXT_HTML))
                .andExpect(content().string(containsString("Admin - Sessions")))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("admin1"), "Response should contain username");
        assertTrue(content.contains("Sessions") || content.contains("sessions"), 
                "Response should contain sessions information");
    }

    /**
     * Test the admin sessions endpoint redirects when not authenticated.
     */
    @Test
    public void testAdminSessionsEndpointRedirects() throws Exception {
        mockMvc.perform(get("/admin/sessions"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    /**
     * Test the admin sessions endpoint returns forbidden for regular users.
     */
    @Test
    @WithUserDetails("user1")
    public void testAdminSessionsEndpointForbidden() throws Exception {
        mockMvc.perform(get("/admin/sessions"))
                .andExpect(status().isForbidden());
    }

    /**
     * Test the admin chat history endpoint as an admin user.
     */
    @Test
    @WithUserDetails("admin1")
    public void testAdminChatHistoryEndpoint() throws Exception {
        MvcResult result = mockMvc.perform(get("/admin/chat/history"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TEXT_HTML))
                .andExpect(content().string(containsString("Admin - Chat History")))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("admin1"), "Response should contain username");
        assertTrue(content.contains("History") || content.contains("history"), 
                "Response should contain chat history information");
    }

    /**
     * Test the admin chat history endpoint with session ID as an admin user.
     */
    @Test
    @WithUserDetails("admin1")
    public void testAdminChatHistoryEndpointWithSessionId() throws Exception {
        MvcResult result = mockMvc.perform(get("/admin/chat/history/test-session-id"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TEXT_HTML))
                .andExpect(content().string(containsString("Admin - Chat History")))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("admin1"), "Response should contain username");
        assertTrue(content.contains("History") || content.contains("history"), 
                "Response should contain chat history information");
        assertTrue(content.contains("test-session-id"), 
                "Response should contain the session ID");
    }

    /**
     * Test the admin users endpoint as an admin user.
     */
    @Test
    @WithUserDetails("admin1")
    public void testAdminUsersEndpoint() throws Exception {
        MvcResult result = mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TEXT_HTML))
                .andExpect(content().string(containsString("Admin - Users")))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("admin1"), "Response should contain username");
        assertTrue(content.contains("Users") || content.contains("users"), 
                "Response should contain users information");
    }

    /**
     * Test that webjars content is accessible.
     * This test verifies that the Bulma CSS file can be accessed via the webjars path.
     */
    @Test
    public void testWebjarsContent() throws Exception {
        mockMvc.perform(get("/webjars/bulma/1.0.3/css/bulma.min.css"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.valueOf("text/css")));
    }
}
