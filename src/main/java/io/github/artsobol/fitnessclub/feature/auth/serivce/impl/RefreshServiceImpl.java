package io.github.artsobol.fitnessclub.feature.auth.serivce.impl;

import io.github.artsobol.fitnessclub.feature.auth.dto.response.AuthResponse;
import io.github.artsobol.fitnessclub.feature.auth.dto.response.RotatedRefresh;
import io.github.artsobol.fitnessclub.feature.auth.serivce.api.RefreshService;
import io.github.artsobol.fitnessclub.feature.auth.serivce.api.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshServiceImpl implements RefreshService {

    private final RefreshTokenService refreshTokenService;
    private final AuthResponseFactory authResponseFactory;

    @Override
    @Transactional
    public AuthResponse refresh(String rawRefreshToken) {
        RotatedRefresh rotated = refreshTokenService.rotate(rawRefreshToken);
        return authResponseFactory.createWithRefresh(rotated.user(), rotated.rawRefreshToken());
    }
}

