package io.github.artsobol.fitnessclub.feature.workout.service;

import io.github.artsobol.fitnessclub.feature.workout.entity.Workout;

public interface WorkoutService {

    Workout findById(Long workoutId);
}
