package io.github.artsobol.fitnessclub.feature.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record RegistrationRequest(
        @Email(message = "auth.user.email.invalid")
        @NotBlank(message = "auth.user.email.blank")
        String email,
        @NotBlank(message = "auth.user.password.blank")
        String password,
        @NotBlank(message = "auth.user.password.confirm.blank")
        String confirmPassword,
        @NotBlank(message = "auth.user.firstname.blank")
        String firstName,
        @NotBlank(message = "auth.user.lastname.blank")
        String lastName,
        @NotNull(message = "auth.user.birthdate.null")
        @Past(message = "auth.user.birthdate.past")
        LocalDate birthdate
) {
}
