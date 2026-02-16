package io.github.artsobol.fitnessclub.feature.trainer.dto;

import io.github.artsobol.fitnessclub.feature.trainerspecialization.dto.TrainerSpecializationResponse;

import java.util.UUID;

public record TrainerResponse(
        UUID id,
        String firstName,
        String lastName,
        TrainerSpecializationResponse title
) {
}
