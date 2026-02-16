package io.github.artsobol.fitnessclub.feature.trainer.service;

import io.github.artsobol.fitnessclub.feature.trainer.entity.TrainerProfile;

import java.util.UUID;

public interface TrainerReader {
    TrainerProfile getTrainerProfile(UUID trainerId);
}
