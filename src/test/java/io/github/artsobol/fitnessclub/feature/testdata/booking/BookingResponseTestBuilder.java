package io.github.artsobol.fitnessclub.feature.testdata.booking;

import io.github.artsobol.fitnessclub.feature.booking.dto.BookingResponse;
import io.github.artsobol.fitnessclub.feature.booking.entity.BookingStatus;
import io.github.artsobol.fitnessclub.feature.testdata.user.UserResponseTestBuilder;
import io.github.artsobol.fitnessclub.feature.testdata.workout.WorkoutResponseTestBuilder;
import io.github.artsobol.fitnessclub.feature.user.dto.UserResponse;
import io.github.artsobol.fitnessclub.feature.workout.dto.response.WorkoutResponse;

public final class BookingResponseTestBuilder {

    private Long id = 1L;
    private WorkoutResponse workout = WorkoutResponseTestBuilder.defaultResponse();
    private UserResponse user = UserResponseTestBuilder.defaultResponse();
    private BookingStatus status = BookingStatus.BOOKED;

    private BookingResponseTestBuilder() {}

    public static BookingResponseTestBuilder builder() {
        return new BookingResponseTestBuilder();
    }

    public static BookingResponse defaultResponse() {
        return builder().build();
    }

    public BookingResponseTestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public BookingResponseTestBuilder withWorkout(WorkoutResponse workout) {
        this.workout = workout;
        return this;
    }

    public BookingResponseTestBuilder withUser(UserResponse user) {
        this.user = user;
        return this;
    }

    public BookingResponseTestBuilder withStatus(BookingStatus status) {
        this.status = status;
        return this;
    }

    public BookingResponse build() {
        return new BookingResponse(id, workout, user, status);
    }
}