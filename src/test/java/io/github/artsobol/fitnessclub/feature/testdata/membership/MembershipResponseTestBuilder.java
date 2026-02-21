package io.github.artsobol.fitnessclub.feature.testdata.membership;

import io.github.artsobol.fitnessclub.feature.membership.dto.response.MembershipResponse;
import io.github.artsobol.fitnessclub.feature.membership.entity.MembershipStatus;
import io.github.artsobol.fitnessclub.feature.testdata.user.UserResponseTestBuilder;
import io.github.artsobol.fitnessclub.feature.user.dto.UserResponse;

import java.time.LocalDate;

public final class MembershipResponseTestBuilder {

    private MembershipResponseTestBuilder() {
    }

    private Long id = 1L;
    private UserResponse user = UserResponseTestBuilder.defaultResponse();
    private LocalDate startsAt = LocalDate.of(2026, 1, 1);
    private LocalDate endsAt = LocalDate.of(2027, 1, 1);
    private MembershipStatus status = MembershipStatus.PENDING;

    public static MembershipResponseTestBuilder builder() {
        return new MembershipResponseTestBuilder();
    }

    public  static MembershipResponse defaultResponse() {
        return builder().build();
    }

    public MembershipResponseTestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public MembershipResponseTestBuilder withUser(UserResponse user) {
        this.user = user;
        return this;
    }

    public MembershipResponseTestBuilder withStartsAt(LocalDate startsAt) {
        this.startsAt = startsAt;
        return this;
    }

    public MembershipResponseTestBuilder withEndsAt(LocalDate endsAt) {
        this.endsAt = endsAt;
        return this;
    }

    public MembershipResponseTestBuilder withStatus(MembershipStatus status) {
        this.status = status;
        return this;
    }

    public MembershipResponse build() {
        return new MembershipResponse(id, user, status, startsAt, endsAt);
    }
}
