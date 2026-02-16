package io.github.artsobol.fitnessclub.feature.trainerspecialization.service;

import io.github.artsobol.fitnessclub.feature.trainerspecialization.dto.TrainerSpecializationCreateRequest;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.dto.TrainerSpecializationResponse;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.dto.TrainerSpecializationUpdateRequest;

import java.util.List;

public interface TrainerSpecializationUseCase {

    List<TrainerSpecializationResponse> getAll();

    TrainerSpecializationResponse getById(Long id);

    TrainerSpecializationResponse create(TrainerSpecializationCreateRequest request);

    TrainerSpecializationResponse update(Long id, TrainerSpecializationUpdateRequest request);

    void delete(Long id);
}
