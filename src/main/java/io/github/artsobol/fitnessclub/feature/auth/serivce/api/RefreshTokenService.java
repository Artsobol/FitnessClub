package io.github.artsobol.fitnessclub.feature.auth.serivce.api;

import io.github.artsobol.fitnessclub.feature.auth.dto.RotatedRefresh;
import io.github.artsobol.fitnessclub.feature.user.dto.User;

import java.util.UUID;

public interface RefreshTokenService {

    String createRefreshToken(User user, UUID sessionId);

    RotatedRefresh rotate(String rawRefreshToken);
}
