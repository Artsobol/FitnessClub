package io.github.artsobol.fitnessclub.feature.trainerspecialization.service;

import io.github.artsobol.fitnessclub.exception.http.ConflictException;
import io.github.artsobol.fitnessclub.exception.http.NotFoundException;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.dto.request.TrainerSpecializationCreateRequest;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.dto.request.TrainerSpecializationUpdateRequest;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.dto.response.TrainerSpecializationResponse;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.entity.TrainerSpecialization;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.mapper.TrainerSpecializationMapper;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.repository.TrainerSpecializationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerSpecializationServiceImplTest {

    @Mock TrainerSpecializationRepository repository;
    @Mock TrainerSpecializationMapper mapper;

    @InjectMocks TrainerSpecializationServiceImpl service;

    @Test
    @DisplayName("Get all: active specializations exist - returns list")
    void getAll_activeSpecializationsExist_returnsList() {
        // given
        TrainerSpecialization first = new TrainerSpecialization();
        TrainerSpecialization second = new TrainerSpecialization();

        TrainerSpecializationResponse firstResponse = new TrainerSpecializationResponse(1L, "Trainer");
        TrainerSpecializationResponse secondResponse = new TrainerSpecializationResponse(2L, "Senior");

        when(repository.findAllByActiveTrue()).thenReturn(List.of(first, second));
        when(mapper.toResponse(first)).thenReturn(firstResponse);
        when(mapper.toResponse(second)).thenReturn(secondResponse);

        // when
        List<TrainerSpecializationResponse> result = service.getAll();

        // then
        assertEquals(List.of(firstResponse, secondResponse), result);

        verify(repository).findAllByActiveTrue();
        verify(mapper).toResponse(first);
        verify(mapper).toResponse(second);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    @DisplayName("Get by id: specialization exists - returns specialization")
    void getById_specializationExists_returnsSpecialization() {
        // given
        long id = 1L;

        TrainerSpecialization entity = new TrainerSpecialization();
        TrainerSpecializationResponse response = new TrainerSpecializationResponse(id, "Trainer");

        when(repository.findByIdAndActiveTrue(id)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        // when
        TrainerSpecializationResponse result = service.getById(id);

        // then
        assertNotNull(result);
        assertEquals(response, result);

        verify(repository).findByIdAndActiveTrue(id);
        verify(mapper).toResponse(entity);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    @DisplayName("Get by id: specialization not found - throws NotFoundException")
    void getById_specializationNotFound_throwsNotFound() {
        // given
        long id = 1L;
        when(repository.findByIdAndActiveTrue(id)).thenReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> service.getById(id));

        // then
        verify(repository).findByIdAndActiveTrue(id);
        verifyNoMoreInteractions(repository);

        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Create: title is unique - specialization is saved and returned")
    void create_titleIsUnique_returnsCreatedSpecialization() {
        // given
        TrainerSpecializationCreateRequest request = new TrainerSpecializationCreateRequest("Trainer");

        TrainerSpecialization entity = new TrainerSpecialization();
        TrainerSpecialization saved = new TrainerSpecialization();

        TrainerSpecializationResponse response = new TrainerSpecializationResponse(1L, "Trainer");

        when(repository.existsByTitleAndActiveTrue(request.title())).thenReturn(false);
        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(response);

        // when
        TrainerSpecializationResponse result = service.create(request);

        // then
        assertNotNull(result);
        assertEquals(response, result);

        verify(repository).existsByTitleAndActiveTrue(request.title());
        verify(mapper).toEntity(request);
        verify(repository).save(entity);
        verify(mapper).toResponse(saved);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    @DisplayName("Create: title already exists - throws ConflictException")
    void create_titleExists_throwsConflict() {
        // given
        TrainerSpecializationCreateRequest request = new TrainerSpecializationCreateRequest("Trainer");
        when(repository.existsByTitleAndActiveTrue(request.title())).thenReturn(true);

        // when
        assertThrows(ConflictException.class, () -> service.create(request));

        // then
        verify(repository).existsByTitleAndActiveTrue(request.title());
        verifyNoMoreInteractions(repository);

        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Update: title already exists - throws ConflictException")
    void update_titleExists_throwsConflict() {
        // given
        long id = 1L;
        TrainerSpecializationUpdateRequest request = new TrainerSpecializationUpdateRequest("Trainer");
        when(repository.existsByTitleAndActiveTrue(request.title())).thenReturn(true);

        // when
        assertThrows(ConflictException.class, () -> service.update(id, request));

        // then
        verify(repository).existsByTitleAndActiveTrue(request.title());
        verifyNoMoreInteractions(repository);

        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Update: title is null - updates entity and returns response")
    void update_titleIsNull_updatesAndReturnsResponse() {
        // given
        long id = 1L;
        TrainerSpecializationUpdateRequest request = new TrainerSpecializationUpdateRequest(null);

        TrainerSpecialization entity = new TrainerSpecialization();
        TrainerSpecializationResponse response = new TrainerSpecializationResponse(id, "Trainer");

        when(repository.findByIdAndActiveTrue(id)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        // when
        TrainerSpecializationResponse result = service.update(id, request);

        // then
        assertNotNull(result);
        assertEquals(response, result);

        verify(repository).findByIdAndActiveTrue(id);
        verify(mapper).update(entity, request);
        verify(mapper).toResponse(entity);

        verify(repository, never()).existsByTitleAndActiveTrue(anyString());

        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    @DisplayName("Deactivate: specialization exists - sets inactive and returns response")
    void deactive_specializationExists_setsInactiveAndReturnsResponse() {
        // given
        long id = 1L;

        TrainerSpecialization entity = new TrainerSpecialization();
        entity.setActive(true);

        TrainerSpecializationResponse response = new TrainerSpecializationResponse(id, "Trainer");

        when(repository.findByIdAndActiveTrue(id)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        // when
        TrainerSpecializationResponse result = service.deactive(id);

        // then
        assertNotNull(result);
        assertEquals(response, result);
        assertFalse(entity.isActive());

        verify(repository).findByIdAndActiveTrue(id);
        verify(mapper).toResponse(entity);
        verifyNoMoreInteractions(repository, mapper);
    }
}