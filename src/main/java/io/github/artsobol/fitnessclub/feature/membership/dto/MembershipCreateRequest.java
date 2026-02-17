package io.github.artsobol.fitnessclub.feature.membership.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record MembershipCreateRequest(
        @NotNull(message = "membership.starts-at.null")
        LocalDate startsAt,
        @NotNull(message = "membership.ends-at.null")
        LocalDate endsAt
) {
}
