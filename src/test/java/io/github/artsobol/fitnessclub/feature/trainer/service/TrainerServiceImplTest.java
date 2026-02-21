package io.github.artsobol.fitnessclub.feature.trainer.service;

import io.github.artsobol.fitnessclub.exception.http.ConflictException;
import io.github.artsobol.fitnessclub.exception.http.NotFoundException;
import io.github.artsobol.fitnessclub.feature.testdata.trainer.TrainerCreateRequestTestBuilder;
import io.github.artsobol.fitnessclub.feature.testdata.trainer.TrainerResponseTestBuilder;
import io.github.artsobol.fitnessclub.feature.trainer.dto.TrainerCreateRequest;
import io.github.artsobol.fitnessclub.feature.trainer.dto.TrainerResponse;
import io.github.artsobol.fitnessclub.feature.trainer.entity.TrainerProfile;
import io.github.artsobol.fitnessclub.feature.trainer.mapper.TrainerMapper;
import io.github.artsobol.fitnessclub.feature.trainer.repository.TrainerRepository;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.entity.TrainerSpecialization;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.service.TrainerSpecializationReader;
import io.github.artsobol.fitnessclub.feature.user.entity.Role;
import io.github.artsobol.fitnessclub.feature.user.entity.User;
import io.github.artsobol.fitnessclub.feature.user.service.UserUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @Mock TrainerSpecializationReader trainerSpecializationReader;
    @Mock UserUseCase userUseCase;
    @Mock TrainerRepository trainerRepository;
    @Mock TrainerMapper trainerMapper;

    @InjectMocks TrainerServiceImpl service;

    @Test
    @DisplayName("Create trainer: profile not exists - profile is saved and returned")
    void createTrainer_profileNotExists_returnsCreatedProfile() {
        // given
        TrainerCreateRequest request = TrainerCreateRequestTestBuilder.defaultRequest();
        UUID userId = request.userId();

        TrainerSpecialization specialization = new TrainerSpecialization();
        User user = new User();

        TrainerProfile saved = new TrainerProfile();

        TrainerResponse response = TrainerResponseTestBuilder.builder()
                .withId(userId)
                .build();

        when(trainerRepository.existsById(userId)).thenReturn(false);
        when(userUseCase.changeRole(userId, Role.TRAINER)).thenReturn(user);
        when(trainerSpecializationReader.getSpecializationById(request.specializationId()))
                .thenReturn(specialization);
        when(trainerRepository.save(any(TrainerProfile.class))).thenReturn(saved);
        when(trainerMapper.toResponse(saved)).thenReturn(response);

        // when
        TrainerResponse result = service.createTrainer(request);

        // then
        assertNotNull(result);
        assertEquals(response, result);

        ArgumentCaptor<TrainerProfile> captor = ArgumentCaptor.forClass(TrainerProfile.class);
        verify(trainerRepository).save(captor.capture());

        TrainerProfile toSave = captor.getValue();
        assertSame(user, toSave.getUser());
        assertSame(specialization, toSave.getSpecialization());

        verify(trainerRepository).existsById(userId);
        verify(userUseCase).changeRole(userId, Role.TRAINER);
        verify(trainerSpecializationReader).getSpecializationById(request.specializationId());
        verify(trainerMapper).toResponse(saved);

        verifyNoMoreInteractions(trainerRepository, userUseCase, trainerSpecializationReader, trainerMapper);
    }

    @Test
    @DisplayName("Create trainer: profile already exists - throws ConflictException")
    void createTrainer_profileExists_throwsConflict() {
        // given
        UUID userId = UUID.randomUUID();
        TrainerCreateRequest request = new TrainerCreateRequest(userId, 1L);

        when(trainerRepository.existsById(userId)).thenReturn(true);

        // when
        assertThrows(ConflictException.class, () -> service.createTrainer(request));

        // then
        verify(trainerRepository).existsById(userId);
        verifyNoMoreInteractions(trainerRepository);

        verifyNoInteractions(userUseCase, trainerSpecializationReader, trainerMapper);
    }

    @Test
    @DisplayName("Get trainers: trainers exist - returns trainers list")
    void getTrainers_trainersExist_returnsTrainersList() {
        // given
        TrainerProfile first = new TrainerProfile();
        TrainerProfile second = new TrainerProfile();

        TrainerResponse firstResponse = new TrainerResponse(UUID.randomUUID(), "John", "Doe", null);
        TrainerResponse secondResponse = new TrainerResponse(UUID.randomUUID(), "Mike", "Vize", null);

        when(trainerRepository.findAllWithDetails()).thenReturn(List.of(first, second));
        when(trainerMapper.toResponse(first)).thenReturn(firstResponse);
        when(trainerMapper.toResponse(second)).thenReturn(secondResponse);

        // when
        List<TrainerResponse> result = service.getTrainers();

        // then
        assertEquals(List.of(firstResponse, secondResponse), result);

        verify(trainerRepository).findAllWithDetails();
        verify(trainerMapper).toResponse(first);
        verify(trainerMapper).toResponse(second);
        verifyNoMoreInteractions(trainerRepository, trainerMapper);

        verifyNoInteractions(userUseCase, trainerSpecializationReader);
    }

    @Test
    @DisplayName("Get trainer by id: trainer exists - returns trainer")
    void getTrainerById_trainerExists_returnsTrainer() {
        // given
        UUID trainerId = UUID.randomUUID();

        TrainerProfile profile = new TrainerProfile();
        TrainerResponse response = new TrainerResponse(trainerId, "John", "Doe", null);

        when(trainerRepository.findById(trainerId)).thenReturn(Optional.of(profile));
        when(trainerMapper.toResponse(profile)).thenReturn(response);

        // when
        TrainerResponse result = service.getTrainerById(trainerId);

        // then
        assertNotNull(result);
        assertEquals(response, result);

        verify(trainerRepository).findById(trainerId);
        verify(trainerMapper).toResponse(profile);
        verifyNoMoreInteractions(trainerRepository, trainerMapper);

        verifyNoInteractions(userUseCase, trainerSpecializationReader);
    }

    @Test
    @DisplayName("Get trainer by id: trainer not found - throws NotFoundException")
    void getTrainerById_trainerNotFound_throwsNotFound() {
        // given
        UUID trainerId = UUID.randomUUID();
        when(trainerRepository.findById(trainerId)).thenReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> service.getTrainerById(trainerId));

        // then
        verify(trainerRepository).findById(trainerId);
        verifyNoMoreInteractions(trainerRepository);

        verifyNoInteractions(trainerMapper, userUseCase, trainerSpecializationReader);
    }
}
