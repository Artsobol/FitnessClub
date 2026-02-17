package io.github.artsobol.fitnessclub.feature.booking.service;

import io.github.artsobol.fitnessclub.exception.http.NotFoundException;
import io.github.artsobol.fitnessclub.feature.booking.entity.Booking;
import io.github.artsobol.fitnessclub.feature.booking.repository.BookingRepository;
import io.github.artsobol.fitnessclub.feature.user.entity.User;
import io.github.artsobol.fitnessclub.feature.user.service.UserService;
import io.github.artsobol.fitnessclub.feature.workout.entity.Workout;
import io.github.artsobol.fitnessclub.feature.workout.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingCommandService {

    private final BookingRepository repository;
    private final UserService userService;
    private final WorkoutService workoutService;

    public Booking book(Long workoutId, UUID userId) {
        User user = userService.findById(userId);
        Workout workout = workoutService.findById(workoutId);

        Booking booking = repository.findByWorkoutIdAndUserId(workoutId, userId)
                .orElseGet(Booking::new);

        booking.setUser(user);
        booking.setWorkout(workout);
        booking.setBooked();

        return repository.save(booking);
    }

    public Booking cancel(Long workoutId, UUID userId) {
        Booking booking = repository.findByWorkoutIdAndUserId(workoutId, userId)
                .orElseThrow(() -> new NotFoundException("booking.not.found", workoutId, userId));

        booking.setCancel();
        return booking;
    }

    public Booking findByWorkoutIdAndUserId(Long workoutId, UUID userId) {
        return repository.findByWorkoutIdAndUserId(workoutId, userId).orElseThrow(
                () -> new NotFoundException("booking.not.found", workoutId, userId)
        );
    }

    public Booking findByBookingId(Long bookingId) {
       return repository.findById(bookingId).orElseThrow(
                () -> new NotFoundException("booking.not.found", bookingId)
        );
    }
}
