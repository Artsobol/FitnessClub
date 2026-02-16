package io.github.artsobol.fitnessclub.feature.trainerspecialization.dto;

import jakarta.validation.constraints.NotBlank;

public record TrainerSpecializationCreateRequest(
        @NotBlank(message = "trainer.specialization.title.blank")
        String title
) {
}
