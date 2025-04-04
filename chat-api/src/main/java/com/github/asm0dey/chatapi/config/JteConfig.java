package com.github.asm0dey.chatapi.config;

import gg.jte.generated.precompiled.StaticTemplates;
import gg.jte.generated.precompiled.Templates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for JTE templates.
 */
@Configuration
public class JteConfig {

    /**
     * Creates a Templates bean for JTE models.
     *
     * @return the Templates instance
     */
    @Bean
    public Templates templates() {
        return new StaticTemplates();
    }
}