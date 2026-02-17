package io.github.artsobol.fitnessclub.feature.booking.service;

import io.github.artsobol.fitnessclub.feature.booking.dto.BookingResponse;

import java.util.List;
import java.util.UUID;

public interface ClientBookingUseCase {

    BookingResponse book(UUID userId, Long workoutId);

    BookingResponse cancel(UUID userId, Long workoutId);

    List<BookingResponse> getClientBookings(UUID userId);

    BookingResponse getClientBooking(UUID userId, Long workoutId);
}
