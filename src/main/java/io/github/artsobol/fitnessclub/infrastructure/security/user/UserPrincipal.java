package io.github.artsobol.fitnessclub.infrastructure.security.user;

import io.github.artsobol.fitnessclub.feature.user.entity.Role;

import java.util.UUID;

public record UserPrincipal(
        UUID userId,
        String username,
        Role role
) {
}
