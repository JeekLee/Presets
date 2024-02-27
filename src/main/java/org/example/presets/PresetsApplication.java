package org.example.presets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PresetsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PresetsApplication.class, args);
    }

}
