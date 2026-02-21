package io.github.artsobol.fitnessclub.feature.workout.service;

import io.github.artsobol.fitnessclub.exception.http.ConflictException;
import io.github.artsobol.fitnessclub.exception.http.NotFoundException;
import io.github.artsobol.fitnessclub.feature.testdata.workout.WorkoutRequestTestBuilder;
import io.github.artsobol.fitnessclub.feature.testdata.workout.WorkoutResponseTestBuilder;
import io.github.artsobol.fitnessclub.feature.trainer.entity.TrainerProfile;
import io.github.artsobol.fitnessclub.feature.trainer.service.TrainerReader;
import io.github.artsobol.fitnessclub.feature.workout.dto.request.WorkoutRequest;
import io.github.artsobol.fitnessclub.feature.workout.dto.response.WorkoutResponse;
import io.github.artsobol.fitnessclub.feature.workout.entity.Workout;
import io.github.artsobol.fitnessclub.feature.workout.entity.WorkoutStatus;
import io.github.artsobol.fitnessclub.feature.workout.mapper.WorkoutMapper;
import io.github.artsobol.fitnessclub.feature.workout.repository.WorkoutRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceImplTest {

    @Mock WorkoutMapper workoutMapper;
    @Mock TrainerReader trainerService;
    @Mock WorkoutRepository workoutRepository;

    @InjectMocks WorkoutServiceImpl service;

    @Test
    @DisplayName("Create workout: valid request - workout is saved")
    void createWorkout_validRequest_returnsSavedWorkout() {
        // given
        UUID trainerId = UUID.randomUUID();
        WorkoutRequest request = WorkoutRequestTestBuilder.defaultRequest();
        TrainerProfile profile = new TrainerProfile();
        Workout entity = new Workout();
        entity.setStartsAt(request.startsAt());
        entity.setEndsAt(request.endsAt());
        WorkoutResponse response = WorkoutResponseTestBuilder.defaultResponse();

        when(workoutRepository.hasIntersectionsByTrainer(trainerId, request.startsAt(), request.endsAt())).thenReturn(
                false);
        when(trainerService.getTrainerProfile(trainerId)).thenReturn(profile);
        when(workoutMapper.toEntity(request)).thenReturn(entity);
        when(workoutRepository.save(any(Workout.class))).thenReturn(entity);
        when(workoutMapper.toResponse(entity)).thenReturn(response);

        // when
        WorkoutResponse result = service.createWorkout(trainerId, request);

        // then
        ArgumentCaptor<Workout> captor = ArgumentCaptor.forClass(Workout.class);
        verify(workoutRepository).save(captor.capture());

        Workout saved = captor.getValue();

        assertEquals(WorkoutStatus.PLANNED, saved.getStatus());
        assertSame(profile, saved.getTrainerProfile());

        assertEquals(request.startsAt(), saved.getStartsAt());
        assertEquals(request.endsAt(), saved.getEndsAt());

        assertNotNull(result);
        assertEquals(response.id(), result.id());
        assertEquals(response.title(), result.title());
        assertEquals(response.description(), result.description());
        assertEquals(response.startsAt(), result.startsAt());
        assertEquals(response.endsAt(), result.endsAt());
        assertEquals(response.capacity(), result.capacity());
        assertEquals(response.status(), result.status());

        verify(workoutRepository).hasIntersectionsByTrainer(trainerId, request.startsAt(), request.endsAt());
        verify(trainerService).getTrainerProfile(trainerId);
        verify(workoutMapper).toEntity(request);
        verify(workoutMapper).toResponse(entity);
        verifyNoMoreInteractions(workoutMapper, trainerService, workoutRepository);
    }

    @Test
    @DisplayName("Create workout: workout intersects - throws ConflictException")
    void createWorkout_workoutIntersects_throwsConflict() {
        // given
        UUID trainerId = UUID.randomUUID();
        WorkoutRequest request = WorkoutRequestTestBuilder.defaultRequest();
        when(workoutRepository.hasIntersectionsByTrainer(trainerId, request.startsAt(), request.endsAt())).thenReturn(
                true);

        // when
        ConflictException ex = assertThrows(ConflictException.class, () -> service.createWorkout(trainerId, request));

        // then
        assertEquals("workout.intersections", ex.getMessage());

        verify(workoutRepository).hasIntersectionsByTrainer(trainerId, request.startsAt(), request.endsAt());
        verifyNoInteractions(trainerService, workoutMapper);
        verifyNoMoreInteractions(workoutRepository);
    }

    @Test
    @DisplayName("Cancel workout: trainer has workout - workout is cancelled and returned")
    void cancelWorkout_trainerHasWorkout_returnsCanceledWorkout() {
        // given
        UUID trainerId = UUID.randomUUID();
        Long workoutId = 10L;
        Workout entity = new Workout();
        entity.setStatus(WorkoutStatus.PLANNED);
        WorkoutResponse response = WorkoutResponseTestBuilder.builder().withStatus(WorkoutStatus.CANCELLED).build();

        when(workoutRepository.hasWorkout(trainerId, workoutId)).thenReturn(true);
        when(workoutRepository.findById(workoutId)).thenReturn(Optional.of(entity));
        when(workoutMapper.toResponse(entity)).thenReturn(response);

        // when
        WorkoutResponse result = service.cancelWorkout(trainerId, workoutId);

        // then
        assertEquals(WorkoutStatus.CANCELLED, entity.getStatus());
        assertSame(response, result);

        verify(workoutRepository).hasWorkout(trainerId, workoutId);
        verify(workoutRepository).findById(workoutId);
        verify(workoutMapper).toResponse(entity);
        verifyNoMoreInteractions(workoutRepository, workoutMapper);
    }

    @Test
    @DisplayName("Cancel workout: workout not found - throws NotFoundException")
    void cancelWorkout_workoutNotFound_throwsNotFound() {
        // given
        UUID trainerId = UUID.randomUUID();
        Long workoutId = 10L;

        when(workoutRepository.hasWorkout(trainerId, workoutId)).thenReturn(false);

        // when
        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.cancelWorkout(trainerId, workoutId));

        // then
        assertEquals("workout.trainer.not.found", ex.getMessage());

        verify(workoutRepository).hasWorkout(trainerId, workoutId);
        verifyNoMoreInteractions(workoutRepository);

    }
}
