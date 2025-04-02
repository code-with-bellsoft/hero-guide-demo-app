package com.github.asm0dey.chatapi.controller;

import com.github.asm0dey.chatapi.repository.ChatSessionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
     * Test the index endpoint.
     */
    @Test
    public void testIndexEndpoint() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    /**
     * Test the index endpoint with a different name.
     */
    @Test
    public void testChatEndpoint() throws Exception {
        mockMvc.perform(get("/chat"))
                .andExpect(status().isOk())
                .andExpect(view().name("chat-view"));
    }

    /**
     * Test that webjars content is accessible.
     * This test verifies that the Bulma CSS file can be accessed via the webjars path.
     */
    @Test
    public void testWebjarsContent() throws Exception {
        System.out.println("[DEBUG_LOG] Testing webjars content accessibility");
        mockMvc.perform(get("/webjars/bulma/1.0.3/css/bulma.min.css"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.valueOf("text/css")));
    }
}
