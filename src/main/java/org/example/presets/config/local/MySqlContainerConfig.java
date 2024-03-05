package org.example.presets.config.local;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@Profile("local")
@Configuration
public class MySqlContainerConfig {
    private static final String MYSQL_DOCKER_IMAGE = "mysql:8";

    static {
        MySQLContainer<?> MYSQL_CONTAINER =
                new MySQLContainer<>(DockerImageName.parse(MYSQL_DOCKER_IMAGE))
                        .withExposedPorts(3306)
                        .withReuse(true);

        MYSQL_CONTAINER.start();

        System.setProperty("spring.datasource.url", MYSQL_CONTAINER.getJdbcUrl());
        System.setProperty("spring.datasource.username", MYSQL_CONTAINER.getUsername());
        System.setProperty("spring.datasource.password", MYSQL_CONTAINER.getPassword());
        System.setProperty("spring.datasource.driver-class-name", MYSQL_CONTAINER.getJdbcDriverInstance().getClass().getName());
    }
}
