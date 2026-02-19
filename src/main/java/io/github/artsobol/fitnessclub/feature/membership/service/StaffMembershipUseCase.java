package io.github.artsobol.fitnessclub.feature.membership.service;

import io.github.artsobol.fitnessclub.feature.membership.dto.request.MembershipCreateRequest;
import io.github.artsobol.fitnessclub.feature.membership.dto.response.MembershipResponse;

import java.util.List;
import java.util.UUID;

public interface StaffMembershipUseCase {

    List<MembershipResponse> getAllByUserId(UUID userId);

    MembershipResponse getActiveByUserId(UUID userId);

    MembershipResponse create(UUID userId, MembershipCreateRequest request);
}
