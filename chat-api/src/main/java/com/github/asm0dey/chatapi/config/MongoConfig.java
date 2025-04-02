package com.github.asm0dey.chatapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * MongoDB configuration for the Chat API.
 * Enables MongoDB auditing and validation.
 */
@Configuration
@EnableMongoAuditing
public class MongoConfig {

    /**
     * Configures MongoDB validation using Bean Validation API.
     * This allows using annotations like @NotNull, @Size, etc. on model classes.
     */
    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener(
            LocalValidatorFactoryBean factory) {
        return new ValidatingMongoEventListener(factory);
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }
}