package io.github.artsobol.fitnessclub.feature.trainer.service;

import io.github.artsobol.fitnessclub.feature.trainer.dto.TrainerCreateRequest;
import io.github.artsobol.fitnessclub.feature.trainer.dto.TrainerResponse;

import java.util.List;
import java.util.UUID;

public interface TrainerUseCase {

    List<TrainerResponse> getTrainers();

    TrainerResponse getTrainerById(UUID trainerId);

    TrainerResponse createTrainer(TrainerCreateRequest request);
}
