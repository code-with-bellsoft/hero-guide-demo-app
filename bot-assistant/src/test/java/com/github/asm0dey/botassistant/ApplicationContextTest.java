package com.github.asm0dey.botassistant;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class ApplicationContextTest {
    @Container
    @ServiceConnection
    static RedisContainer redis = new RedisContainer("redis");

    @Test
    void contextLoads() {
    }


}
