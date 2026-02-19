package io.github.artsobol.fitnessclub.feature.workout.service;

import io.github.artsobol.fitnessclub.feature.workout.dto.request.WorkoutRequest;
import io.github.artsobol.fitnessclub.feature.workout.dto.response.WorkoutResponse;
import io.github.artsobol.fitnessclub.feature.workout.dto.request.WorkoutUpdateRequest;

import java.util.UUID;

public interface WorkoutUseCase {

    WorkoutResponse createWorkout(UUID userId, WorkoutRequest request);

    WorkoutResponse updateWorkout(UUID userId, Long workoutId, WorkoutUpdateRequest request);

    WorkoutResponse cancelWorkout(UUID userId, Long workoutId);
}
