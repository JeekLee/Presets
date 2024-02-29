package org.example.presets.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "redis.redisson")
@Getter
@Setter
public class RedissonProperties {
    private String host;
    private int port;
}
