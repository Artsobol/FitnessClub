package io.github.artsobol.fitnessclub.feature.auth.dto;

public record EncodedRefreshToken(
        String rawToken,
        RefreshToken refreshToken
) {
}
