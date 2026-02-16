package io.github.artsobol.fitnessclub.feature.trainerspecialization.repository;

import io.github.artsobol.fitnessclub.feature.trainerspecialization.entity.TrainerSpecialization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainerSpecializationRepository extends JpaRepository<TrainerSpecialization, Long> {

    List<TrainerSpecialization> findAllByActiveTrue();

    Optional<TrainerSpecialization> findByIdAndActiveTrue(Long id);

    boolean existsByTitleAndActiveTrue(String title);
}
