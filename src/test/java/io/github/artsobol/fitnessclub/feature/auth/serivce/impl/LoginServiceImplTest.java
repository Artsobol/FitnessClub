package io.github.artsobol.fitnessclub.feature.auth.serivce.impl;

import io.github.artsobol.fitnessclub.exception.security.AuthenticationException;
import io.github.artsobol.fitnessclub.feature.auth.dto.request.LoginRequest;
import io.github.artsobol.fitnessclub.feature.auth.dto.response.AuthResponse;
import io.github.artsobol.fitnessclub.feature.auth.dto.response.UserInfo;
import io.github.artsobol.fitnessclub.feature.user.entity.Role;
import io.github.artsobol.fitnessclub.feature.user.entity.User;
import io.github.artsobol.fitnessclub.feature.user.service.UserUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceImplTest {

    @Mock UserUseCase userUseCase;
    @Mock PasswordEncoder passwordEncoder;
    @Mock AuthResponseFactory authResponseFactory;

    @InjectMocks LoginServiceImpl service;

    @Test
    @DisplayName("Login: valid credentials - returns auth response")
    void login_validCredentials_returnsAuthResponse() {
        // given
        LoginRequest request = new LoginRequest("user@example.com", "password");

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("user@example.com");
        user.setRole(Role.CLIENT);
        user.setPasswordHash("hash");

        AuthResponse expectedResponse = new AuthResponse(
                "access",
                "refresh",
                new UserInfo(user.getId(), user.getEmail(), user.getRole())
        );

        when(userUseCase.findUserByUsername("user@example.com")).thenReturn(user);
        when(passwordEncoder.matches("password", "hash")).thenReturn(true);
        when(authResponseFactory.create(any(User.class), any(UUID.class))).thenReturn(expectedResponse);

        // when
        AuthResponse result = service.login(request);

        // then
        assertNotNull(result);
        assertEquals(expectedResponse, result);

        verify(userUseCase).findUserByUsername("user@example.com");
        verify(passwordEncoder).matches("password", "hash");
        verify(authResponseFactory).create(any(User.class), any(UUID.class));

        verifyNoMoreInteractions(userUseCase, passwordEncoder, authResponseFactory);
    }

    @Test
    @DisplayName("Login: invalid credentials - throws AuthenticationException")
    void login_invalidCredentials_throwsAuthenticationException() {
        // given
        LoginRequest request = new LoginRequest("user@example.com", "password");

        User user = new User();
        user.setPasswordHash("hash");

        when(userUseCase.findUserByUsername("user@example.com")).thenReturn(user);
        when(passwordEncoder.matches("password", "hash")).thenReturn(false);

        // when / then
        assertThrows(AuthenticationException.class, () -> service.login(request));

        verify(userUseCase).findUserByUsername("user@example.com");
        verify(passwordEncoder).matches("password", "hash");

        verifyNoMoreInteractions(userUseCase, passwordEncoder, authResponseFactory);
    }
}