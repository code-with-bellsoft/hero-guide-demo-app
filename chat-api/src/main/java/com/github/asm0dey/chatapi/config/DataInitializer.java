package com.github.asm0dey.chatapi.config;

import com.github.asm0dey.chatapi.model.User;
import com.github.asm0dey.chatapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Configuration class to initialize default data on application startup.
 */
@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Value("${app.create-test-users:true}")
    private boolean createTestUsers;

    /**
     * Creates default users if they don't exist.
     *
     * @param userRepository  the user repository
     * @param passwordEncoder the password encoder
     * @return a CommandLineRunner that initializes data
     */
    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Always create admin user
            createDefaultUserIfNotExists(userRepository, passwordEncoder, "admin1", "adminpass1", Set.of("ADMIN"));

            // Create test users only if enabled
            if (createTestUsers) {
                createDefaultUserIfNotExists(userRepository, passwordEncoder, "user1", "pass1");
                createDefaultUserIfNotExists(userRepository, passwordEncoder, "user2", "pass2");
                createDefaultUserIfNotExists(userRepository, passwordEncoder, "user3", "pass3");
                log.info("Test users initialized");
            } else {
                log.info("Test user creation is disabled");
            }

            log.info("User initialization completed");
        };
    }

    /**
     * Creates a default user if it doesn't exist.
     *
     * @param userRepository  the user repository
     * @param passwordEncoder the password encoder
     * @param username        the username
     * @param password        the password
     * @param roles           the roles to assign to the user
     */
    private void createDefaultUserIfNotExists(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                              String username, String password, Set<String> roles) {
        if (!userRepository.existsByUsername(username)) {
            User user = User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .email(username + "@example.com")
                    .displayName(username)
                    .roles(roles)
                    .enabled(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            userRepository.save(user);
            log.info("Created default user: {} with roles: {}", username, roles);
        } else {
            log.info("Default user already exists: {}", username);
        }
    }

    /**
     * Creates a default user if it doesn't exist with USER role.
     *
     * @param userRepository  the user repository
     * @param passwordEncoder the password encoder
     * @param username        the username
     * @param password        the password
     */
    private void createDefaultUserIfNotExists(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                              String username, String password) {
        createDefaultUserIfNotExists(userRepository, passwordEncoder, username, password, Set.of("USER"));
    }
}
