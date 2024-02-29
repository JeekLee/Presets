package org.example.presets.config;

import lombok.RequiredArgsConstructor;
import org.example.presets.config.properties.RedissonProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class RedissonConfig {
    private final RedissonProperties redissonProperties;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        // Use rediss for SSL
        config.useSingleServer().setAddress("redis://"+redissonProperties.getHost()+":" + redissonProperties.getPort());
        return Redisson.create(config);
    }
}
