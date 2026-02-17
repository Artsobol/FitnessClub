package io.github.artsobol.fitnessclub.feature.membership.dto;

import java.time.LocalDate;

public record MembershipUpdateRequest(
        LocalDate startsAt,
        LocalDate endsAt
) {
}
