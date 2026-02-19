package io.github.artsobol.fitnessclub.feature.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email(message = "auth.user.email.invalid")
        @NotBlank(message = "auth.user.email.blank")
        String email,
        @NotBlank(message = "auth.user.password.blank")
        String password
) {
}
