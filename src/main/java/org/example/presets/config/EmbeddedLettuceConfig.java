package org.example.presets.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

@Profile("local")
@Configuration
public class EmbeddedLettuceConfig {
    @Value("${redis.lettuce.port}")
    private int redisPort;

    @Value("${redis.lettuce.host}")
    private String redisHost;

    private RedisServer redisServer;

    @PostConstruct
    public void startLettuceRedis() {
        this.redisServer = RedisServer.builder()
                .port(redisPort)
                .setting("maxmemory 128M")
                .build();
        this.redisServer.start();
    }
    @PreDestroy
    public void stopLettuceRedis() {
        this.redisServer.stop();
    }
}
