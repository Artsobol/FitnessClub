package io.github.artsobol.fitnessclub.infrastructure.security.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security.session")
public record SessionProperties(
        int maxSessions
) {
}
