package io.github.artsobol.fitnessclub.feature.workout.service;

import io.github.artsobol.fitnessclub.exception.http.ConflictException;
import io.github.artsobol.fitnessclub.exception.http.NotFoundException;
import io.github.artsobol.fitnessclub.feature.trainer.entity.TrainerProfile;
import io.github.artsobol.fitnessclub.feature.trainer.service.TrainerReader;
import io.github.artsobol.fitnessclub.feature.workout.dto.WorkoutRequest;
import io.github.artsobol.fitnessclub.feature.workout.dto.WorkoutResponse;
import io.github.artsobol.fitnessclub.feature.workout.dto.WorkoutUpdateRequest;
import io.github.artsobol.fitnessclub.feature.workout.entity.Workout;
import io.github.artsobol.fitnessclub.feature.workout.entity.WorkoutStatus;
import io.github.artsobol.fitnessclub.feature.workout.mapper.WorkoutMapper;
import io.github.artsobol.fitnessclub.feature.workout.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('TRAINER', 'MANAGER', 'ADMIN')")
public class WorkoutServiceImpl implements WorkoutUseCase, WorkoutService{

    private final WorkoutMapper workoutMapper;
    private final TrainerReader trainerService;
    private final WorkoutRepository workoutRepository;

    @Override
    public Workout findById(Long id) {
        return workoutRepository.findById(id).orElseThrow(
                () -> new NotFoundException("workout.not.found")
        );
    }

    @Override
    @Transactional
    public WorkoutResponse createWorkout(UUID trainerId, WorkoutRequest request) {
        ensureNotIntersections(trainerId, request.startsAt(), request.endsAt());

        TrainerProfile profile = trainerService.getTrainerProfile(trainerId);
        Workout entity = setWorkout(request, profile);
        workoutRepository.save(entity);

        return workoutMapper.toResponse(entity);
    }

    @Override
    @Transactional
    public WorkoutResponse updateWorkout(UUID trainerId, Long workoutId, WorkoutUpdateRequest request) {
        Workout entity = findById(workoutId);
        ensureHasWorkout(trainerId, workoutId);

        Instant newStarts = request.startsAt() != null ? request.startsAt() : entity.getStartsAt();
        Instant newEnds   = request.endsAt()   != null ? request.endsAt()   : entity.getEndsAt();
        ensureNotIntersections(trainerId, newStarts, newEnds);

        workoutMapper.update(request, entity);

        return workoutMapper.toResponse(entity);
    }

    @Override
    @Transactional
    public WorkoutResponse cancelWorkout(UUID trainerId, Long workoutId) {
        ensureHasWorkout(trainerId, workoutId);

        Workout entity = findById(workoutId);
        entity.setStatus(WorkoutStatus.CANCELLED);

        return workoutMapper.toResponse(entity);
    }

    private Workout setWorkout(WorkoutRequest request, TrainerProfile profile) {
        Workout entity = workoutMapper.toEntity(request);
        entity.setStatus(WorkoutStatus.PLANNED);
        entity.setTrainerProfile(profile);
        return entity;
    }

    private void ensureHasWorkout(UUID trainerId, Long workoutId){
        if (!workoutRepository.hasWorkout(trainerId, workoutId)) {
            throw new NotFoundException("workout.trainer.not.found", trainerId);
        }
    }

    private void ensureNotIntersections(UUID trainerId, Instant from, Instant to) {
        if (workoutRepository.hasIntersectionsByTrainer(trainerId, from, to)) {
            throw new ConflictException("workout.intersections");
        }
    }
}
