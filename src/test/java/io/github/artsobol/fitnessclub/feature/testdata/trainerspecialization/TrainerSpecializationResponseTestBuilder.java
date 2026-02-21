package io.github.artsobol.fitnessclub.feature.testdata.trainerspecialization;

import io.github.artsobol.fitnessclub.feature.trainerspecialization.dto.response.TrainerSpecializationResponse;

public final class TrainerSpecializationResponseTestBuilder {

    private Long id = 1L;
    private String title = "Trainer";

    private TrainerSpecializationResponseTestBuilder() {}

    public static TrainerSpecializationResponseTestBuilder builder(){
        return new TrainerSpecializationResponseTestBuilder();
    }

    public static TrainerSpecializationResponse defaultResponse(){
        return builder().build();
    }

    public TrainerSpecializationResponseTestBuilder withId(Long id){
        this.id = id;
        return this;
    }

    public TrainerSpecializationResponseTestBuilder withTitle(String title){
        this.title = title;
        return this;
    }

    public TrainerSpecializationResponse build(){
        return new TrainerSpecializationResponse(id, title);
    }
}
