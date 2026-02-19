package io.github.artsobol.fitnessclub.feature.workout.dto.request;

import java.time.Instant;

public record WorkoutUpdateRequest(
        String title,
        String description,
        Instant startsAt,
        Instant endsAt,
        Integer capacity
) {
}
