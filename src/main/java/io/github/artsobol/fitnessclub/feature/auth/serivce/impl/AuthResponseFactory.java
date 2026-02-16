package io.github.artsobol.fitnessclub.feature.auth.serivce.impl;

import io.github.artsobol.fitnessclub.feature.auth.dto.AuthResponse;
import io.github.artsobol.fitnessclub.feature.auth.dto.UserInfo;
import io.github.artsobol.fitnessclub.feature.auth.serivce.api.AccessTokenService;
import io.github.artsobol.fitnessclub.feature.auth.serivce.api.RefreshTokenService;
import io.github.artsobol.fitnessclub.feature.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthResponseFactory {

    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;

    public AuthResponse create(User user, UUID sessionId) {
        return new AuthResponse(
                accessTokenService.createAccessToken(user),
                refreshTokenService.createRefreshToken(user, sessionId),
                new UserInfo(user.getId(), user.getEmail(), user.getRole())
        );
    }

    public AuthResponse createWithRefresh(User user, String rawRefreshToken) {
        return new AuthResponse(
                accessTokenService.createAccessToken(user),
                rawRefreshToken,
                new UserInfo(user.getId(), user.getEmail(), user.getRole())
        );
    }

}
