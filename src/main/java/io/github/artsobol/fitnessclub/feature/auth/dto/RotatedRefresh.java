package io.github.artsobol.fitnessclub.feature.auth.dto;

import io.github.artsobol.fitnessclub.feature.user.entity.User;

public record RotatedRefresh(User user, String rawRefreshToken) {
}