package io.github.artsobol.fitnessclub.feature.trainerspecialization.service;

import io.github.artsobol.fitnessclub.feature.trainerspecialization.entity.TrainerSpecialization;

public interface TrainerSpecializationReader {

    TrainerSpecialization getSpecializationById(Long id);
}
