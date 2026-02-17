package io.github.artsobol.fitnessclub.feature.workout.web;

import io.github.artsobol.fitnessclub.feature.workout.dto.WorkoutRequest;
import io.github.artsobol.fitnessclub.feature.workout.dto.WorkoutResponse;
import io.github.artsobol.fitnessclub.feature.workout.dto.WorkoutUpdateRequest;
import io.github.artsobol.fitnessclub.feature.workout.service.WorkoutUseCase;
import io.github.artsobol.fitnessclub.infrastructure.security.user.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/workouts", produces = MediaType.APPLICATION_JSON_VALUE)
public class WorkoutController {

    private final WorkoutUseCase service;

    @PostMapping
    public ResponseEntity<WorkoutResponse> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody WorkoutRequest request
    ) {
        WorkoutResponse response = service.createWorkout(principal.userId(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<WorkoutResponse> update(
            @PathVariable("id") Long workoutId,
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody WorkoutUpdateRequest request
    ) {
        WorkoutResponse response = service.updateWorkout(principal.userId(), workoutId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<WorkoutResponse> cancel(
            @PathVariable("id") Long workoutId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        WorkoutResponse response = service.cancelWorkout(principal.userId(), workoutId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}