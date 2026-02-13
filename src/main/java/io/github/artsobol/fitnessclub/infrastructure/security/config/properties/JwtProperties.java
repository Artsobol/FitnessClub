package io.github.artsobol.fitnessclub.infrastructure.security.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.security.jwt")
public record JwtProperties(
        String secret,
        Duration accessTokenExpiration
) {
}
