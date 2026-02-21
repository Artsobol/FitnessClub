package io.github.artsobol.fitnessclub.feature.testdata.trainer;

import io.github.artsobol.fitnessclub.feature.testdata.trainerspecialization.TrainerSpecializationResponseTestBuilder;
import io.github.artsobol.fitnessclub.feature.trainer.dto.TrainerResponse;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.dto.response.TrainerSpecializationResponse;

import java.util.UUID;

public final class TrainerResponseTestBuilder {

    private UUID id = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private String firstName = "John";
    private String lastName = "Doe";
    private TrainerSpecializationResponse specialization = TrainerSpecializationResponseTestBuilder.defaultResponse();

    private TrainerResponseTestBuilder() {
    }

    public static TrainerResponseTestBuilder builder(){
        return new TrainerResponseTestBuilder();
    }

    public static TrainerResponse defaultResponse(){
        return builder().build();
    }

    public TrainerResponseTestBuilder withId(UUID id){
        this.id = id;
        return this;
    }

    public TrainerResponseTestBuilder withFirstName(String firstName){
        this.firstName = firstName;
        return this;
    }

    public TrainerResponseTestBuilder withLastName(String lastName){
        this.lastName = lastName;
        return this;
    }

    public TrainerResponseTestBuilder withSpecialization(TrainerSpecializationResponse specialization){
        this.specialization = specialization;
        return this;
    }

    public TrainerResponse build(){
        return new TrainerResponse(id, firstName, lastName, specialization);
    }
}
