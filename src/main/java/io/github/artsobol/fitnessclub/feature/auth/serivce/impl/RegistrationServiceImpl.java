package io.github.artsobol.fitnessclub.feature.auth.serivce.impl;

import io.github.artsobol.fitnessclub.exception.http.ConflictException;
import io.github.artsobol.fitnessclub.feature.auth.dto.AuthResponse;
import io.github.artsobol.fitnessclub.feature.auth.dto.RegistrationRequest;
import io.github.artsobol.fitnessclub.feature.auth.serivce.api.RegistrationService;
import io.github.artsobol.fitnessclub.feature.user.dto.User;
import io.github.artsobol.fitnessclub.feature.user.dto.UserCreateRequest;
import io.github.artsobol.fitnessclub.feature.user.repository.UserRepository;
import io.github.artsobol.fitnessclub.feature.user.service.api.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final AuthResponseFactory authResponseFactory;

    @Override
    public AuthResponse register(RegistrationRequest request) {
        ensureEmailNotExists(request.email());
        ensurePasswordNotMismatch(request.password(), request.confirmPassword());

        UserCreateRequest userRequest = new UserCreateRequest(
                request.email(),
                passwordEncoder(request.password()),
                request.firstName(),
                request.lastName(),
                request.birthdate()
        );
        User user = userService.createUser(userRequest);

        UUID sessionId = UUID.randomUUID();
        return authResponseFactory.create(user, sessionId);
    }

    private void ensurePasswordNotMismatch(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new ConflictException("auth.password.mismatch");
        }
    }

    private void ensureEmailNotExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("auth.email.exists");
        }
    }

    private String passwordEncoder(String password) {
        return passwordEncoder.encode(password);
    }
}
