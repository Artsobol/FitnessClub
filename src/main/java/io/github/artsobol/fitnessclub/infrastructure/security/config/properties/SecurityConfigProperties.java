package io.github.artsobol.fitnessclub.infrastructure.security.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security.config")
public record SecurityConfigProperties(
        String passwordEncoder,
        Integer bCryptStrength
) {
}
