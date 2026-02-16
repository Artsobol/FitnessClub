package io.github.artsobol.fitnessclub.infrastructure.security.jwt;

import io.github.artsobol.fitnessclub.feature.user.entity.Role;

import java.util.UUID;

public record JwtSubject(
        UUID userId,
        String username,
        Role role
) {
}
