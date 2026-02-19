package io.github.artsobol.fitnessclub.feature.membership.service;

import io.github.artsobol.fitnessclub.feature.membership.dto.response.MembershipResponse;
import io.github.artsobol.fitnessclub.feature.membership.dto.request.MembershipUpdateRequest;

import java.util.List;

public interface ManagementMembershipUseCase {

    List<MembershipResponse> getAll();

    MembershipResponse getById(Long membershipId);

    MembershipResponse update(Long membershipId, MembershipUpdateRequest request);

    MembershipResponse activate(Long membershipId);
}
