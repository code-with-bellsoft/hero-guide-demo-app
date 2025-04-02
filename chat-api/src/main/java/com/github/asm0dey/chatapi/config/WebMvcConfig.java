package com.github.asm0dey.chatapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration for Spring MVC.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * Configure resource handlers for static resources.
     *
     * @param registry the ResourceHandlerRegistry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configure webjars resource handler
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .setCachePeriod(3600) // Cache for 1 hour
                .resourceChain(true);
    }
}
