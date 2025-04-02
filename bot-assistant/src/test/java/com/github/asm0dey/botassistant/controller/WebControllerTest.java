package com.github.asm0dey.botassistant.controller;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

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
        System.out.println("[DEBUG_LOG] Testing index endpoint");
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    /**
     * Test the assistant endpoint.
     */
    @Test
    public void testAssistantEndpoint() throws Exception {
        System.out.println("[DEBUG_LOG] Testing assistant endpoint");
        mockMvc.perform(get("/assistant"))
                .andExpect(status().isOk())
                .andExpect(view().name("assistant-view"));
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
