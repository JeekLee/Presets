package org.example.presets.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@Profile("local")
@Configuration
public class LocalContainerLettuceConfig {
    private static final String REDIS_DOCKER_IMAGE = "redis:5.0.3-alpine";

    static {
        GenericContainer<?> LETTUCE_CONTAINER =
                new GenericContainer<>(DockerImageName.parse(REDIS_DOCKER_IMAGE))
                        .withExposedPorts(6379)
                        .withReuse(true);

        System.setProperty("redis.lettuce.host", LETTUCE_CONTAINER.getHost());
        System.setProperty("redis.lettuce.port", LETTUCE_CONTAINER.getMappedPort(6379).toString());
    }


}
