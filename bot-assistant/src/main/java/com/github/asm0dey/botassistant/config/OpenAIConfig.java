package com.github.asm0dey.botassistant.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for OpenAI API integration.
 * <p>
 * Note: Spring AI auto-configuration will handle the creation of the OpenAI client
 * based on properties in application.properties.
 */
@Configuration
public class OpenAIConfig {

    @Value("${spring.ai.openai.api-key:}")
    private String openaiApiKey;

    @Value("${spring.ai.openai.model:gpt-3.5-turbo}")
    private String model;

    @Value("${spring.ai.openai.temperature:0.7}")
    private double temperature;

    @Value("${spring.ai.openai.max-tokens:500}")
    private int maxTokens;

    // Spring AI auto-configuration will create the necessary beans
    // based on the properties defined in application.properties
}
