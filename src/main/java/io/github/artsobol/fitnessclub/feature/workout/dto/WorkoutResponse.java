package io.github.artsobol.fitnessclub.feature.workout.dto;

import io.github.artsobol.fitnessclub.feature.trainer.dto.TrainerResponse;
import io.github.artsobol.fitnessclub.feature.workout.entity.WorkoutStatus;

import java.time.Instant;

public record WorkoutResponse(
        Long id,
        String title,
        String description,
        TrainerResponse trainer,
        Instant startsAt,
        Instant endsAt,
        Integer capacity,
        WorkoutStatus status
) {
}
