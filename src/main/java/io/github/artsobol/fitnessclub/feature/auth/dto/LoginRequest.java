package io.github.artsobol.fitnessclub.feature.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "user.email.blank")
        @Email(message = "user.email.invalid")
        String email,
        @NotBlank(message = "user.password.blank")
        String password
) {
}
