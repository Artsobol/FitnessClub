package io.github.artsobol.fitnessclub.feature.auth.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        UserInfo user
) {

    public AuthResponse withoutRefreshToken(){
        return new AuthResponse(accessToken, null, user);
    }
}
