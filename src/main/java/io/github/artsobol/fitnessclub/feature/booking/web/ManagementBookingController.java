package io.github.artsobol.fitnessclub.feature.booking.web;

import io.github.artsobol.fitnessclub.feature.booking.dto.BookingResponse;
import io.github.artsobol.fitnessclub.feature.booking.service.ManagementBookingUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ManagementBookingController {

    private final ManagementBookingUseCase useCase;

    @GetMapping("/bookings")
    public ResponseEntity<List<BookingResponse>> getAll() {
        List<BookingResponse> response = useCase.getAll();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/bookings/{bookingId}")
    public ResponseEntity<BookingResponse> getById(@PathVariable Long bookingId) {
        BookingResponse response = useCase.getById(bookingId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/workouts/{workoutId}/bookings")
    public ResponseEntity<List<BookingResponse>> getAllByWorkoutId(@PathVariable Long workoutId) {
        List<BookingResponse> response = useCase.getAllByWorkoutId(workoutId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/users/{userId}/bookings")
    public ResponseEntity<List<BookingResponse>> getAllByUserId(@PathVariable UUID userId) {
        List<BookingResponse> response = useCase.getAllByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/users/{userId}/workouts/{workoutId}/book")
    public ResponseEntity<BookingResponse> bookForUser(
            @PathVariable("userId") UUID userId,
            @PathVariable("workoutId") Long workoutId
    ) {
        BookingResponse response = useCase.bookForUser(userId, workoutId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/users/{userId}/workouts/{workoutId}/book/cancel")
    public ResponseEntity<BookingResponse> cancelForUser(
            @PathVariable("userId") UUID userId,
            @PathVariable("workoutId") Long workoutId
    ) {
        BookingResponse response = useCase.cancelForUser(userId, workoutId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
