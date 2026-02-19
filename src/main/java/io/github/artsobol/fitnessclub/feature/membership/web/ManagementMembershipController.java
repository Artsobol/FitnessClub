package io.github.artsobol.fitnessclub.feature.membership.web;

import io.github.artsobol.fitnessclub.feature.membership.dto.response.MembershipResponse;
import io.github.artsobol.fitnessclub.feature.membership.dto.request.MembershipUpdateRequest;
import io.github.artsobol.fitnessclub.feature.membership.service.ManagementMembershipUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/memberships")
@RequiredArgsConstructor
public class ManagementMembershipController {

    private final ManagementMembershipUseCase useCase;

    @GetMapping
    public ResponseEntity<List<MembershipResponse>> getAll() {
        List<MembershipResponse> response = useCase.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{membershipId}")
    public ResponseEntity<MembershipResponse> getById(@PathVariable Long membershipId) {
        MembershipResponse response = useCase.getById(membershipId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{membershipId}")
    public ResponseEntity<MembershipResponse> update(
            @PathVariable Long membershipId,
            @RequestBody MembershipUpdateRequest request
    ) {
        MembershipResponse response = useCase.update(membershipId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{membershipId}/activate")
    public ResponseEntity<MembershipResponse> activate(@PathVariable Long membershipId) {
        MembershipResponse response = useCase.activate(membershipId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
