package io.github.artsobol.fitnessclub.feature.membership.web;

import io.github.artsobol.fitnessclub.feature.membership.dto.MembershipResponse;
import io.github.artsobol.fitnessclub.feature.membership.service.ClientMembershipUseCase;
import io.github.artsobol.fitnessclub.infrastructure.security.user.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/memberships")
@RequiredArgsConstructor
public class ClientMembershipController {

    private final ClientMembershipUseCase useCase;

    @GetMapping("/my/active")
    public ResponseEntity<MembershipResponse> getMyActive(@AuthenticationPrincipal UserPrincipal principal) {
        MembershipResponse response = useCase.getMyActive(principal.userId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
