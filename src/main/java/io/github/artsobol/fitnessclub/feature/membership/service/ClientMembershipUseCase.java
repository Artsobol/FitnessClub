package io.github.artsobol.fitnessclub.feature.membership.service;

import io.github.artsobol.fitnessclub.feature.membership.dto.MembershipResponse;

import java.util.UUID;

public interface ClientMembershipUseCase {

    MembershipResponse getMyActive(UUID userId);
}
