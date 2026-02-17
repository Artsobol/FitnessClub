package io.github.artsobol.fitnessclub.feature.user.dto;

import java.time.LocalDate;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        LocalDate birthdate
) {
}
