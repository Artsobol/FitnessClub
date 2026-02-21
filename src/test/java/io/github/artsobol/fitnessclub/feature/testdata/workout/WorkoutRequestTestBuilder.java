package io.github.artsobol.fitnessclub.feature.testdata.workout;

import io.github.artsobol.fitnessclub.feature.workout.dto.request.WorkoutRequest;

import java.time.Instant;

public final class WorkoutRequestTestBuilder {

    private WorkoutRequestTestBuilder() {
    }

    private String title = "Workout title";
    private String description = "Workout description";
    private Instant startsAt = Instant.parse("2024-01-01T00:00:00Z");
    private Instant endsAt = Instant.parse("2024-01-01T01:00:00Z");
    private Integer capacity = 10;

    public static WorkoutRequestTestBuilder builder() {
        return new WorkoutRequestTestBuilder();
    }

    public static WorkoutRequest defaultRequest() {
        return builder().build();
    }

    public WorkoutRequestTestBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public WorkoutRequestTestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public WorkoutRequestTestBuilder withStartsAt(Instant startsAt) {
        this.startsAt = startsAt;
        return this;
    }

    public WorkoutRequestTestBuilder withEndsAt(Instant endsAt) {
        this.endsAt = endsAt;
        return this;
    }

    public WorkoutRequestTestBuilder withCapacity(Integer capacity) {
        this.capacity = capacity;
        return this;
    }

    public WorkoutRequest build() {
        return new WorkoutRequest(title, description, startsAt, endsAt, capacity);
    }
}
