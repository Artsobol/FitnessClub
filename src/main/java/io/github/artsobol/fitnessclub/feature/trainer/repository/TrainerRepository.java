package io.github.artsobol.fitnessclub.feature.trainer.repository;

import io.github.artsobol.fitnessclub.feature.trainer.entity.TrainerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TrainerRepository extends JpaRepository<TrainerProfile, UUID> {

    boolean existsById(UUID id);

    @Query("""
                select t
                from TrainerProfile t
                join fetch t.user
                join fetch t.specialization
            """)
    List<TrainerProfile> findAllWithDetails();
}
