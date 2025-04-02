package com.github.asm0dey.chatapi.config;

import com.github.asm0dey.chatapi.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the Chat API.
 * Configures authentication and authorization.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configures security filter chain.
     * For this demo, we'll use basic authentication for REST endpoints
     * and permit WebSocket endpoints for authenticated users.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for WebSocket
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/ws/**").permitAll() // Allow WebSocket connections
                        .requestMatchers("/api/public/**").permitAll() // Public API endpoints
                        .requestMatchers("/api/users/register").permitAll() // Allow user registration
                        .anyRequest().authenticated() // All other requests need authentication
                )
                .userDetailsService(userDetailsService) // Use our custom UserDetailsService
                .httpBasic(httpBasic -> {}) // Enable HTTP Basic Authentication
                .build();
    }

    /**
     * Password encoder for secure password storage.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
