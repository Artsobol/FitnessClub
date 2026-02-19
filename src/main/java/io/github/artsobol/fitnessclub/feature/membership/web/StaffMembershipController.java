package io.github.artsobol.fitnessclub.feature.membership.web;

import io.github.artsobol.fitnessclub.feature.membership.dto.request.MembershipCreateRequest;
import io.github.artsobol.fitnessclub.feature.membership.dto.response.MembershipResponse;
import io.github.artsobol.fitnessclub.feature.membership.service.StaffMembershipUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users/{userId}/memberships")
@RequiredArgsConstructor
public class StaffMembershipController {

    private final StaffMembershipUseCase useCase;

    @GetMapping
    public ResponseEntity<List<MembershipResponse>> getAllByUserId(@PathVariable UUID userId) {
        List<MembershipResponse> response = useCase.getAllByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/active")
    public ResponseEntity<MembershipResponse> getActiveByUserId(@PathVariable UUID userId) {
        MembershipResponse response = useCase.getActiveByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<MembershipResponse> create(
            @PathVariable UUID userId,
            @RequestBody @Valid MembershipCreateRequest request
    ) {
        MembershipResponse response = useCase.create(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
