package io.github.artsobol.fitnessclub.feature.membership.service;

import io.github.artsobol.fitnessclub.feature.membership.dto.MembershipCreateRequest;
import io.github.artsobol.fitnessclub.feature.membership.dto.MembershipResponse;

import java.util.List;
import java.util.UUID;

public interface StaffMembershipUseCase {

    List<MembershipResponse> getAllByUserId(UUID userId);

    MembershipResponse getActiveByUserId(UUID userId);

    MembershipResponse create(UUID userId, MembershipCreateRequest request);
}
