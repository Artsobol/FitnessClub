package io.github.artsobol.fitnessclub.feature.auth.serivce.impl;

import io.github.artsobol.fitnessclub.exception.http.ConflictException;
import io.github.artsobol.fitnessclub.feature.auth.dto.request.RegistrationRequest;
import io.github.artsobol.fitnessclub.feature.auth.dto.response.AuthResponse;
import io.github.artsobol.fitnessclub.feature.auth.dto.response.UserInfo;
import io.github.artsobol.fitnessclub.feature.user.dto.UserCreateRequest;
import io.github.artsobol.fitnessclub.feature.user.entity.Role;
import io.github.artsobol.fitnessclub.feature.user.entity.User;
import io.github.artsobol.fitnessclub.feature.user.repository.UserRepository;
import io.github.artsobol.fitnessclub.feature.user.service.UserUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {

    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock UserUseCase userUseCase;
    @Mock AuthResponseFactory authResponseFactory;

    @InjectMocks RegistrationServiceImpl service;

    @Test
    @DisplayName("Register: email unique and passwords match - returns auth response")
    void register_emailUniqueAndPasswordsMatch_returnsAuthResponse() {
        // given
        RegistrationRequest request = new RegistrationRequest(
                "user@example.com",
                "password",
                "password",
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1)
        );

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("user@example.com");
        user.setRole(Role.CLIENT);

        AuthResponse expectedResponse = new AuthResponse(
                "access",
                "refresh",
                new UserInfo(user.getId(), user.getEmail(), user.getRole())
        );

        when(userRepository.existsByEmail("user@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("hash");
        when(userUseCase.createUser(any(UserCreateRequest.class))).thenReturn(user);
        when(authResponseFactory.create(any(User.class), any(UUID.class))).thenReturn(expectedResponse);

        // when
        AuthResponse result = service.register(request);

        // then
        assertNotNull(result);
        assertEquals(expectedResponse, result);

        ArgumentCaptor<UserCreateRequest> captor = ArgumentCaptor.forClass(UserCreateRequest.class);
        verify(userUseCase).createUser(captor.capture());
        assertEquals("hash", captor.getValue().passwordHash());
        assertEquals("user@example.com", captor.getValue().email());

        // verify no other interactions
        verifyNoMoreInteractions(userRepository, passwordEncoder, userUseCase, authResponseFactory);
    }

    @Test
    @DisplayName("Register: email already exists - throws ConflictException")
    void register_emailExists_throwsConflictException() {
        // given
        RegistrationRequest request = new RegistrationRequest(
                "user@example.com",
                "password",
                "password",
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1)
        );

        when(userRepository.existsByEmail("user@example.com")).thenReturn(true);

        // when / then
        assertThrows(ConflictException.class, () -> service.register(request));

        // verify no creation occurs
        verify(userUseCase, never()).createUser(any(UserCreateRequest.class));
        verifyNoMoreInteractions(userRepository, userUseCase);
    }

    @Test
    @DisplayName("Register: passwords mismatch - throws ConflictException")
    void register_passwordsMismatch_throwsConflictException() {
        // given
        RegistrationRequest request = new RegistrationRequest(
                "user@example.com",
                "password",
                "different",
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1)
        );

        when(userRepository.existsByEmail("user@example.com")).thenReturn(false);

        // when / then
        assertThrows(ConflictException.class, () -> service.register(request));

        // verify password encoding and user creation don't occur
        verify(passwordEncoder, never()).encode("password");
        verify(userUseCase, never()).createUser(any(UserCreateRequest.class));

        verifyNoMoreInteractions(userRepository, passwordEncoder, userUseCase);
    }
}