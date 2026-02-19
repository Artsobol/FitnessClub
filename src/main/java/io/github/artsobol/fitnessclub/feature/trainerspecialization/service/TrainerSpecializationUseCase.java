package io.github.artsobol.fitnessclub.feature.trainerspecialization.service;

import io.github.artsobol.fitnessclub.feature.trainerspecialization.dto.request.TrainerSpecializationCreateRequest;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.dto.response.TrainerSpecializationResponse;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.dto.request.TrainerSpecializationUpdateRequest;

import java.util.List;

public interface TrainerSpecializationUseCase {

    List<TrainerSpecializationResponse> getAll();

    TrainerSpecializationResponse getById(Long id);

    TrainerSpecializationResponse create(TrainerSpecializationCreateRequest request);

    TrainerSpecializationResponse update(Long id, TrainerSpecializationUpdateRequest request);

    void delete(Long id);
}
