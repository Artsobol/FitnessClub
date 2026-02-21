package io.github.artsobol.fitnessclub.feature.testdata.trainer;

import io.github.artsobol.fitnessclub.feature.trainer.dto.TrainerCreateRequest;

import java.util.UUID;

public final class TrainerCreateRequestTestBuilder {

    private TrainerCreateRequestTestBuilder() {
    }

    private UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private Long specializationId = 1L;

    public static TrainerCreateRequestTestBuilder builder() {
        return new TrainerCreateRequestTestBuilder();
    }

    public static TrainerCreateRequest defaultRequest() {
        return builder().build();
    }

    public TrainerCreateRequestTestBuilder withUserId(UUID userId) {
        this.userId = userId;
        return this;
    }

    public TrainerCreateRequestTestBuilder withSpecializationId(Long specializationId) {
        this.specializationId = specializationId;
        return this;
    }

    public TrainerCreateRequest build(){
        return new TrainerCreateRequest(userId, specializationId);
    }
}
