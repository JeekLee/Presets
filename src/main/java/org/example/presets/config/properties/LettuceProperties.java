package org.example.presets.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "redis.lettuce")
@Getter
@Setter
public class LettuceProperties {
    private String host;
    private int port;
}
