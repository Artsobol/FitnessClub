package io.github.artsobol.fitnessclub.feature.testdata.workout;

import io.github.artsobol.fitnessclub.feature.testdata.trainer.TrainerResponseTestBuilder;
import io.github.artsobol.fitnessclub.feature.trainer.dto.TrainerResponse;
import io.github.artsobol.fitnessclub.feature.workout.dto.response.WorkoutResponse;
import io.github.artsobol.fitnessclub.feature.workout.entity.WorkoutStatus;

import java.time.Instant;

public final class WorkoutResponseTestBuilder {

    private Long id = 1L;
    private String title = "Workout title";
    private String description = "Workout description";
    private TrainerResponse trainer = TrainerResponseTestBuilder.defaultResponse();
    private Instant startsAt = Instant.parse("2024-01-01T00:00:00Z");
    private Instant endsAt = Instant.parse("2024-01-01T01:00:00Z");
    private Integer capacity = 10;
    private WorkoutStatus status = WorkoutStatus.PLANNED;

    private WorkoutResponseTestBuilder() {
    }

    public static WorkoutResponseTestBuilder builder() {
        return new WorkoutResponseTestBuilder();
    }

    public WorkoutResponseTestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public WorkoutResponseTestBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public WorkoutResponseTestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public WorkoutResponseTestBuilder withTrainer(TrainerResponse trainer) {
        this.trainer = trainer;
        return this;
    }

    public WorkoutResponseTestBuilder withStartsAt(Instant startsAt) {
        this.startsAt = startsAt;
        return this;
    }

    public WorkoutResponseTestBuilder withEndsAt(Instant endsAt) {
        this.endsAt = endsAt;
        return this;
    }

    public WorkoutResponseTestBuilder withCapacity(Integer capacity) {
        this.capacity = capacity;
        return this;
    }

    public WorkoutResponseTestBuilder withStatus(WorkoutStatus status) {
        this.status = status;
        return this;
    }

    public static WorkoutResponse defaultResponse() {
        return builder().build();
    }

    public WorkoutResponse build() {
        return new WorkoutResponse(id, title, description, trainer, startsAt, endsAt, capacity, status);
    }
}
