package io.github.artsobol.fitnessclub.feature.booking.web;

import io.github.artsobol.fitnessclub.feature.booking.dto.BookingResponse;
import io.github.artsobol.fitnessclub.feature.booking.service.ClientBookingUseCase;
import io.github.artsobol.fitnessclub.infrastructure.security.user.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class ClientBookingController {

    private final ClientBookingUseCase useCase;

    @GetMapping("/bookings")
    public ResponseEntity<List<BookingResponse>> getClientBookings(@AuthenticationPrincipal UserPrincipal principal){
        List<BookingResponse> response = useCase.getClientBookings(principal.userId());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/workouts/{workoutId}/bookings")
    public ResponseEntity<BookingResponse> getClientBooking(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long workoutId){
        BookingResponse response = useCase.getClientBooking(principal.userId(), workoutId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/workouts/{workoutId}/book")
    public ResponseEntity<BookingResponse> book(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long workoutId){
        BookingResponse response = useCase.book(principal.userId(), workoutId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/workouts/{workoutId}/cancel")
    public ResponseEntity<BookingResponse> cancel(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long workoutId){
        BookingResponse response = useCase.cancel(principal.userId(), workoutId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
