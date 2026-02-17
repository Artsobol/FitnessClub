package io.github.artsobol.fitnessclub.feature.booking.repository;

import io.github.artsobol.fitnessclub.feature.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByWorkoutIdAndUserId(Long workoutId, UUID userId);

    List<Booking> findByUserId(UUID userId);

    List<Booking> findAllByUserId(UUID userId);

    List<Booking> findAllByWorkoutId(Long workoutId);
}
