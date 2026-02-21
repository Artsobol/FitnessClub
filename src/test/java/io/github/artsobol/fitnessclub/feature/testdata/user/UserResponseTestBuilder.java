package io.github.artsobol.fitnessclub.feature.testdata.user;

import io.github.artsobol.fitnessclub.feature.user.dto.UserResponse;

import java.time.LocalDate;
import java.util.UUID;

public final class UserResponseTestBuilder {

    private UserResponseTestBuilder() {
    }

    private UUID id = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private String firstName = "John";
    private String lastName = "Doe";
    private LocalDate birthdate = LocalDate.of(2000, 1, 1);

    public static UserResponseTestBuilder builder() {
        return new UserResponseTestBuilder();
    }

    public static UserResponse defaultResponse() {
        return builder().build();
    }

    public UserResponseTestBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public UserResponseTestBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserResponseTestBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserResponseTestBuilder withBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
        return this;
    }

    public UserResponse build(){
        return new UserResponse(id, firstName, lastName, birthdate);
    }
}
