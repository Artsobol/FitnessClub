package io.github.artsobol.fitnessclub.feature.trainer.dto;

import java.util.UUID;

public record TrainerCreateRequest (
        UUID userId,
        Long specializationId
){
}
