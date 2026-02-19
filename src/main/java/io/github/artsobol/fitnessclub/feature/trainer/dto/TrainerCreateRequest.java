package io.github.artsobol.fitnessclub.feature.trainer.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TrainerCreateRequest (
        @NotNull(message = "trainer.user-id.null")
        UUID userId,
        @NotNull(message = "trainer.specialization-id.null")
        Long specializationId
){
}
