package com.github.asm0dey.botassistant.config;

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
