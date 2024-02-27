package org.example.presets.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

@Profile("local")
@Configuration
public class LocalRedisConfig {
    @Value("${spring.data.redis.port}")
    private Integer redisPort;

    @Value("${spring.data.redis.host}")
    private String redisHost;

    private RedisServer redisServer;

    @PostConstruct
    public void startRedis() {
        this.redisServer = new RedisServer(redisPort);
        this.redisServer.start();
    }
    @PreDestroy
    public void stopRedis() {
        this.redisServer.stop();
    }
}
