package io.github.artsobol.fitnessclub.feature.workout.repository;

import io.github.artsobol.fitnessclub.feature.workout.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.UUID;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    @Query("""
                select (count(w) > 0)
                from Workout w
                where w.trainerProfile.id = :trainerId
                  and w.startsAt < :startsAt
                  and w.endsAt > :endsAt
            """)
    boolean hasIntersectionsByTrainer(
            @Param("trainerId") UUID trainerId,
            @Param("startsAt") Instant startsAt,
            @Param("endsAt") Instant endsAt
    );

    @Query("""
            select (count(w) > 0)
            from Workout w
            where w.trainerProfile.id = :trainerId
            and w.id = :workoutId
            """)
    boolean hasWorkout(@Param("trainerId") UUID trainerId, @Param("workoutId") Long id);
}
