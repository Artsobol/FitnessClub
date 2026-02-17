package io.github.artsobol.fitnessclub.feature.booking.service;

import io.github.artsobol.fitnessclub.feature.booking.dto.BookingResponse;

import java.util.List;
import java.util.UUID;

public interface ManagementBookingUseCase {

    BookingResponse getById(Long bookingId);

    List<BookingResponse> getAll();

    List<BookingResponse> getAllByWorkoutId(Long workoutId);

    List<BookingResponse> getAllByUserId(UUID userId);

    BookingResponse bookForUser(UUID userId, Long workoutId);
    BookingResponse cancelForUser(UUID userId, Long workoutId);
}
