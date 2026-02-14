package io.github.artsobol.fitnessclub.feature.auth.web;

import io.github.artsobol.fitnessclub.feature.auth.dto.AuthResponse;
import io.github.artsobol.fitnessclub.feature.auth.dto.LoginRequest;
import io.github.artsobol.fitnessclub.feature.auth.dto.RegistrationRequest;
import io.github.artsobol.fitnessclub.feature.auth.serivce.api.LoginService;
import io.github.artsobol.fitnessclub.feature.auth.serivce.api.RefreshService;
import io.github.artsobol.fitnessclub.feature.auth.serivce.api.RegistrationService;
import io.github.artsobol.fitnessclub.infrastructure.security.config.properties.CookieProperties;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final CookieProperties properties;
    private final RegistrationService registrationService;
    private final LoginService loginService;
    private final RefreshService refreshService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegistrationRequest request) {
        AuthResponse response = registrationService.register(request);

        return getResponse(getResponseCookie(response), response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = loginService.login(request);

        return getResponse(getResponseCookie(response), response);
    }

    private @NonNull ResponseCookie getResponseCookie(AuthResponse response) {
        return ResponseCookie.from(properties.cookieName(), response.refreshToken())
                .httpOnly(true)
                .secure(properties.secure())
                .sameSite(properties.sameSite())
                .path(properties.path())
                .maxAge(properties.maxAge())
                .build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @CookieValue(value = "refresh_token", required = false) String refreshToken
    ) {
        AuthResponse response = refreshService.refresh(refreshToken);

        return getResponse(getResponseCookie(response), response);
    }

    private static @NonNull ResponseEntity<AuthResponse> getResponse(
            ResponseCookie refreshCookie,
            AuthResponse response
    ) {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(response.withoutRefreshToken());
    }
}