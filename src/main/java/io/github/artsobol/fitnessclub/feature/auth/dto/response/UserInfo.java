package io.github.artsobol.fitnessclub.feature.auth.dto.response;

import io.github.artsobol.fitnessclub.feature.user.entity.Role;

import java.util.UUID;

public record UserInfo(
        UUID id,
        String username,
        Role role
) {
}
