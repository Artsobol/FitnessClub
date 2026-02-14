package io.github.artsobol.fitnessclub.feature.user.dto;

import java.time.LocalDate;

public record UserCreateRequest(
        String email,
        String passwordHash,
        String firstName,
        String lastName,
        LocalDate birthdate
) {
}
