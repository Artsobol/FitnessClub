package io.github.artsobol.fitnessclub.feature.membership.dto;

import io.github.artsobol.fitnessclub.feature.membership.entity.MembershipStatus;
import io.github.artsobol.fitnessclub.feature.user.dto.UserResponse;

import java.time.LocalDate;

public record MembershipResponse(
        Long id,
        UserResponse user,
        MembershipStatus status,
        LocalDate startsAt,
        LocalDate endsAt
) {
}
