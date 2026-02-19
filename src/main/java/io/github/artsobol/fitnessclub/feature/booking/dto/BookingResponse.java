package io.github.artsobol.fitnessclub.feature.booking.dto;

import io.github.artsobol.fitnessclub.feature.booking.entity.BookingStatus;
import io.github.artsobol.fitnessclub.feature.user.dto.UserResponse;
import io.github.artsobol.fitnessclub.feature.workout.dto.response.WorkoutResponse;

public record BookingResponse(
        Long id,
        WorkoutResponse workout,
        UserResponse user,
        BookingStatus status
) {
}
