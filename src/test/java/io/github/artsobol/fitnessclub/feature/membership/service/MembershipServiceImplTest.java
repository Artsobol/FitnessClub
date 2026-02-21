package io.github.artsobol.fitnessclub.feature.membership.service;

import io.github.artsobol.fitnessclub.exception.http.BadRequestException;
import io.github.artsobol.fitnessclub.exception.http.NotFoundException;
import io.github.artsobol.fitnessclub.feature.membership.dto.request.MembershipCreateRequest;
import io.github.artsobol.fitnessclub.feature.membership.dto.request.MembershipUpdateRequest;
import io.github.artsobol.fitnessclub.feature.membership.dto.response.MembershipResponse;
import io.github.artsobol.fitnessclub.feature.membership.entity.Membership;
import io.github.artsobol.fitnessclub.feature.membership.entity.MembershipStatus;
import io.github.artsobol.fitnessclub.feature.membership.mapper.MembershipMapper;
import io.github.artsobol.fitnessclub.feature.membership.repository.MembershipRepository;
import io.github.artsobol.fitnessclub.feature.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MembershipServiceImplTest {

    @Mock MembershipRepository repository;
    @Mock UserService userService;
    @Mock MembershipMapper mapper;

    @InjectMocks MembershipServiceImpl service;

    @Test
    @DisplayName("Get all: memberships exist - returns list")
    void getAll_membershipsExist_returnsList() {
        // given
        LocalDate start1 = LocalDate.of(2026, 1, 1);
        LocalDate end1 = LocalDate.of(2026, 1, 31);
        LocalDate start2 = LocalDate.of(2026, 2, 1);
        LocalDate end2 = LocalDate.of(2026, 2, 11);

        Membership first = new Membership();
        Membership second = new Membership();

        MembershipResponse firstResponse = new MembershipResponse(1L, null, MembershipStatus.ACTIVE, start1, end1);
        MembershipResponse secondResponse = new MembershipResponse(2L, null, MembershipStatus.PENDING, start2, end2);

        when(repository.findAll()).thenReturn(List.of(first, second));
        when(mapper.toResponse(first)).thenReturn(firstResponse);
        when(mapper.toResponse(second)).thenReturn(secondResponse);

        // when
        List<MembershipResponse> result = service.getAll();

        // then
        assertEquals(List.of(firstResponse, secondResponse), result);

        verify(repository).findAll();
        verify(mapper).toResponse(first);
        verify(mapper).toResponse(second);
        verifyNoMoreInteractions(repository, mapper);

        verifyNoInteractions(userService);
    }

    @Test
    @DisplayName("Get by id: membership exists - returns membership")
    void getById_membershipExists_returnsMembership() {
        // given
        long id = 1L;

        Membership entity = new Membership();
        MembershipResponse response = new MembershipResponse(
                id, null, MembershipStatus.ACTIVE,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31)
        );

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        // when
        MembershipResponse result = service.getById(id);

        // then
        assertNotNull(result);
        assertEquals(response, result);

        verify(repository).findById(id);
        verify(mapper).toResponse(entity);
        verifyNoMoreInteractions(repository, mapper);

        verifyNoInteractions(userService);
    }

    @Test
    @DisplayName("Get by id: membership not found - throws NotFoundException")
    void getById_membershipNotFound_throwsNotFound() {
        // given
        long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> service.getById(id));

        // then
        verify(repository).findById(id);
        verifyNoMoreInteractions(repository);

        verifyNoInteractions(mapper, userService);
    }

    @Test
    @DisplayName("Get my active: active membership exists - returns membership")
    void getMyActive_activeMembershipExists_returnsMembership() {
        // given
        UUID userId = UUID.randomUUID();

        Membership entity = new Membership();
        MembershipResponse response = new MembershipResponse(
                1L, null, MembershipStatus.ACTIVE,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31)
        );

        when(repository.findByUserIdAndStatus(userId, MembershipStatus.ACTIVE)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        // when
        MembershipResponse result = service.getMyActive(userId);

        // then
        assertNotNull(result);
        assertEquals(response, result);

        verify(repository).findByUserIdAndStatus(userId, MembershipStatus.ACTIVE);
        verify(mapper).toResponse(entity);
        verifyNoMoreInteractions(repository, mapper);

        verifyNoInteractions(userService);
    }

    @Test
    @DisplayName("Get my active: active membership not found - throws NotFoundException")
    void getMyActive_activeMembershipNotFound_throwsNotFound() {
        // given
        UUID userId = UUID.randomUUID();
        when(repository.findByUserIdAndStatus(userId, MembershipStatus.ACTIVE)).thenReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> service.getMyActive(userId));

        // then
        verify(repository).findByUserIdAndStatus(userId, MembershipStatus.ACTIVE);
        verifyNoMoreInteractions(repository);

        verifyNoInteractions(mapper, userService);
    }

    @Test
    @DisplayName("Get all by user id: memberships exist - returns list")
    void getAllByUserId_membershipsExist_returnsList() {
        // given
        UUID userId = UUID.randomUUID();

        Membership entity = new Membership();
        MembershipResponse response = new MembershipResponse(
                1L, null, MembershipStatus.ACTIVE,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31)
        );

        when(repository.findAllByUserId(userId)).thenReturn(List.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        // when
        List<MembershipResponse> result = service.getAllByUserId(userId);

        // then
        assertEquals(List.of(response), result);

        verify(repository).findAllByUserId(userId);
        verify(mapper).toResponse(entity);
        verifyNoMoreInteractions(repository, mapper);

        verifyNoInteractions(userService);
    }

    @Test
    @DisplayName("Create: start date is not before end date - throws BadRequestException")
    void create_datesInvalid_throwsBadRequest() {
        // given
        UUID userId = UUID.randomUUID();

        LocalDate startsAt = LocalDate.of(2026, 2, 1);
        LocalDate endsAt = LocalDate.of(2026, 2, 1);

        MembershipCreateRequest request = new MembershipCreateRequest(startsAt, endsAt);

        // when
        assertThrows(BadRequestException.class, () -> service.create(userId, request));

        // then
        verifyNoInteractions(repository, userService, mapper);
    }

    @Test
    @DisplayName("Update: membership exists - updates and returns membership")
    void update_membershipExists_updatesAndReturnsMembership() {
        // given
        long id = 1L;

        LocalDate startsAt = LocalDate.of(2026, 1, 1);
        LocalDate endsAt = LocalDate.of(2026, 1, 31);

        Membership entity = new Membership();
        MembershipUpdateRequest request = new MembershipUpdateRequest(startsAt, endsAt);

        MembershipResponse response = new MembershipResponse(id, null, MembershipStatus.ACTIVE, startsAt, endsAt);

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        // when
        MembershipResponse result = service.update(id, request);

        // then
        assertNotNull(result);
        assertEquals(response, result);

        verify(repository).findById(id);
        verify(mapper).update(entity, request);
        verify(mapper).toResponse(entity);
        verifyNoMoreInteractions(repository, mapper);

        verifyNoInteractions(userService);
    }

    @Test
    @DisplayName("Activate: membership exists - sets status active and returns membership")
    void activate_membershipExists_setsActiveAndReturnsMembership() {
        // given
        long id = 1L;

        LocalDate startsAt = LocalDate.of(2026, 1, 1);
        LocalDate endsAt = LocalDate.of(2026, 1, 31);

        Membership entity = new Membership();
        entity.setStatus(MembershipStatus.PENDING);

        MembershipResponse response = new MembershipResponse(id, null, MembershipStatus.ACTIVE, startsAt, endsAt);

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        // when
        MembershipResponse result = service.activate(id);

        // then
        assertNotNull(result);
        assertEquals(response, result);
        assertEquals(MembershipStatus.ACTIVE, entity.getStatus());

        verify(repository).findById(id);
        verify(mapper).toResponse(entity);
        verifyNoMoreInteractions(repository, mapper);

        verifyNoInteractions(userService);
    }
}