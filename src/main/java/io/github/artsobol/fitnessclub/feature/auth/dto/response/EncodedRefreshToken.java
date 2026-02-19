package io.github.artsobol.fitnessclub.feature.auth.dto.response;

import io.github.artsobol.fitnessclub.feature.auth.entity.RefreshToken;

public record EncodedRefreshToken(
        String rawToken,
        RefreshToken refreshToken
) {
}
