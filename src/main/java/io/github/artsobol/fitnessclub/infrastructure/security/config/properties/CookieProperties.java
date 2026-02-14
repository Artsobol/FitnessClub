package io.github.artsobol.fitnessclub.infrastructure.security.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.security.cookie")
public record CookieProperties(
        boolean secure,
        Duration maxAge,
        String sameSite,
        String cookieName,
        String path
) {
}
