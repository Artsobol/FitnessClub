package io.github.artsobol.fitnessclub.feature.auth.dto;

import io.github.artsobol.fitnessclub.feature.user.dto.Role;

import java.util.UUID;

public record UserInfo(
        UUID id,
        String username,
        Role role
) {
}
