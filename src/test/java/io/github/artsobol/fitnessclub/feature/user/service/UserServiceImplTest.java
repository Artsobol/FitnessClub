package io.github.artsobol.fitnessclub.feature.user.service;

import io.github.artsobol.fitnessclub.exception.http.ConflictException;
import io.github.artsobol.fitnessclub.exception.http.NotFoundException;
import io.github.artsobol.fitnessclub.exception.security.AuthenticationException;
import io.github.artsobol.fitnessclub.feature.user.dto.UserCreateRequest;
import io.github.artsobol.fitnessclub.feature.user.entity.Role;
import io.github.artsobol.fitnessclub.feature.user.entity.User;
import io.github.artsobol.fitnessclub.feature.user.mapper.UserMapper;
import io.github.artsobol.fitnessclub.feature.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock UserRepository repository;
    @Mock UserMapper mapper;

    @InjectMocks UserServiceImpl service;

    @Test
    @DisplayName("Create user: default CLIENT role assigned")
    void createUser_withClientRole_returnsCreatedUser() {
        // given
        UserCreateRequest request = new UserCreateRequest(
                "user@example.com",
                "hash",
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1)
        );
        User entity = new User();
        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);

        // when
        User result = service.createUser(request);

        // then
        assertNotNull(result);
        assertEquals(entity, result);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(repository).save(captor.capture());
        assertEquals(Role.CLIENT, captor.getValue().getRole());

        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    @DisplayName("Throw ConflictException when role is already set")
    void shouldThrowConflict_whenRoleAlreadySet() {
        // given
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setRole(Role.MANAGER);
        when(repository.findById(userId)).thenReturn(Optional.of(user));

        // when / then
        assertThrows(ConflictException.class, () -> service.changeRole(userId, Role.MANAGER));

        // then
        verify(repository).findById(userId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Change role: role differs from current role")
    void shouldChangeRole_whenRoleDiffers() {
        // given
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setRole(Role.CLIENT);
        when(repository.findById(userId)).thenReturn(Optional.of(user));

        // when
        User result = service.changeRole(userId, Role.TRAINER);

        // then
        assertNotNull(result);
        assertEquals(user, result);
        assertEquals(Role.TRAINER, user.getRole());

        verify(repository).findById(userId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Throw NotFoundException when user not found by id")
    void shouldThrowNotFound_whenUserNotFoundById() {
        // given
        UUID userId = UUID.randomUUID();
        when(repository.findById(userId)).thenReturn(Optional.empty());

        // when / then
        assertThrows(NotFoundException.class, () -> service.findById(userId));

        // then
        verify(repository).findById(userId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Throw AuthenticationException when user not found by email")
    void shouldThrowAuthentication_whenUserNotFoundByEmail() {
        // given
        String email = "user@example.com";
        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        // when / then
        assertThrows(AuthenticationException.class, () -> service.findUserByUsername(email));

        // then
        verify(repository).findByEmail(email);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Return user when found by email")
    void shouldReturnUser_whenFoundByEmail() {
        // given
        String email = "user@example.com";
        User user = new User();
        when(repository.findByEmail(email)).thenReturn(Optional.of(user));

        // when
        User result = service.findUserByUsername(email);

        // then
        assertNotNull(result);
        assertEquals(user, result);

        verify(repository).findByEmail(email);
        verifyNoMoreInteractions(repository);
    }
}
