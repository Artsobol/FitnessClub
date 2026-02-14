package io.github.artsobol.fitnessclub.feature.auth.serivce.impl;

import io.github.artsobol.fitnessclub.feature.auth.serivce.api.AccessTokenService;
import io.github.artsobol.fitnessclub.feature.user.dto.Role;
import io.github.artsobol.fitnessclub.feature.user.dto.User;
import io.github.artsobol.fitnessclub.infrastructure.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessTokenServiceImpl implements AccessTokenService {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String createAccessToken(User user) {
        Role role = user.getRole();
        return jwtTokenProvider.generateToken(user.getEmail(), role);
    }
}
