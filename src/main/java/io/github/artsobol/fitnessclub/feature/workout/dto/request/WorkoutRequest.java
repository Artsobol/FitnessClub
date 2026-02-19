package io.github.artsobol.fitnessclub.feature.workout.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record WorkoutRequest(
        @NotBlank(message = "workout.title.blank")
        String title,
        String description,
        @NotNull(message = "workout.startsAt.null")
        Instant startsAt,
        @NotNull(message = "workout.endsAt.null")
        Instant endsAt,
        Integer capacity
) {
}
