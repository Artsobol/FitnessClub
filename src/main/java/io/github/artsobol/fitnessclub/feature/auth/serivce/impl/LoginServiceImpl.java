package io.github.artsobol.fitnessclub.feature.auth.serivce.impl;

import io.github.artsobol.fitnessclub.exception.security.AuthenticationException;
import io.github.artsobol.fitnessclub.feature.auth.dto.AuthResponse;
import io.github.artsobol.fitnessclub.feature.auth.dto.LoginRequest;
import io.github.artsobol.fitnessclub.feature.auth.serivce.api.LoginService;
import io.github.artsobol.fitnessclub.feature.user.entity.User;
import io.github.artsobol.fitnessclub.feature.user.service.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserUseCase userUseCase;
    private final PasswordEncoder passwordEncoder;
    private final AuthResponseFactory authResponseFactory;

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userUseCase.findUserByUsername(request.email());
        ensureCredentialsValid(request.password(), user.getPasswordHash());

        UUID sessionId = UUID.randomUUID();
        return authResponseFactory.create(user, sessionId);
    }



    private void ensureCredentialsValid(String password, String confirmPassword) {
        if (!passwordEncoder.matches(password, confirmPassword)) {
            throw new AuthenticationException("auth.bad-credentials");
        }
    }
}
