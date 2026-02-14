package io.github.artsobol.fitnessclub.feature.auth.serivce.impl;

import io.github.artsobol.fitnessclub.feature.auth.dto.EncodedRefreshToken;
import io.github.artsobol.fitnessclub.feature.auth.dto.RefreshToken;
import io.github.artsobol.fitnessclub.feature.user.dto.User;
import io.github.artsobol.fitnessclub.infrastructure.security.config.properties.RefreshTokenProperties;
import io.github.artsobol.fitnessclub.infrastructure.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenEncoder {

    private final RefreshTokenProperties properties;

    public EncodedRefreshToken create(User user, UUID sessionId) {
        RefreshToken token = createTokenWithUserAndExpiresAt(user);
        token.setSessionId(sessionId);

        String rawToken = TokenUtils.generateRawToken(properties.length());
        token.setTokenHash(TokenUtils.hmacSha256Base64Url(rawToken, properties.pepper()));

        return new EncodedRefreshToken(rawToken, token);
    }

    private RefreshToken createTokenWithUserAndExpiresAt(User user){
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setExpiresAt(Instant.now().plus(properties.ttl()));
        token.setLastUsedAt(Instant.now());
        return token;
    }

    public String hash(String rawToken) {
        return TokenUtils.hmacSha256Base64Url(rawToken, properties.pepper());
    }
}
