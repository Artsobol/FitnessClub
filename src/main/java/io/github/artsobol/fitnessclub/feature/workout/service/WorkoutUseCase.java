package io.github.artsobol.fitnessclub.feature.workout.service;

import io.github.artsobol.fitnessclub.feature.workout.dto.WorkoutRequest;
import io.github.artsobol.fitnessclub.feature.workout.dto.WorkoutResponse;
import io.github.artsobol.fitnessclub.feature.workout.dto.WorkoutUpdateRequest;

import java.util.UUID;

public interface WorkoutUseCase {

    WorkoutResponse createWorkout(UUID userId, WorkoutRequest request);

    WorkoutResponse updateWorkout(UUID userId, Long workoutId, WorkoutUpdateRequest request);

    WorkoutResponse cancelWorkout(UUID userId, Long workoutId);
}
