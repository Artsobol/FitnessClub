package io.github.artsobol.fitnessclub.feature.trainer.service;

import io.github.artsobol.fitnessclub.exception.http.ConflictException;
import io.github.artsobol.fitnessclub.exception.http.NotFoundException;
import io.github.artsobol.fitnessclub.feature.trainer.dto.TrainerCreateRequest;
import io.github.artsobol.fitnessclub.feature.trainer.dto.TrainerResponse;
import io.github.artsobol.fitnessclub.feature.trainer.entity.TrainerProfile;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.entity.TrainerSpecialization;
import io.github.artsobol.fitnessclub.feature.trainer.mapper.TrainerMapper;
import io.github.artsobol.fitnessclub.feature.trainer.repository.TrainerRepository;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.service.TrainerSpecializationReader;
import io.github.artsobol.fitnessclub.feature.user.entity.Role;
import io.github.artsobol.fitnessclub.feature.user.entity.User;
import io.github.artsobol.fitnessclub.feature.user.service.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerUseCase, TrainerReader {

    private final TrainerSpecializationReader trainerSpecializationReader;
    private final UserUseCase userUseCase;
    private final TrainerRepository trainerRepository;
    private final TrainerMapper trainerMapper;

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public TrainerResponse createTrainer(TrainerCreateRequest request) {
        TrainerProfile trainerProfile = setTrainer(request);
        TrainerProfile saved = trainerRepository.save(trainerProfile);

        return trainerMapper.toResponse(saved);
    }

    private TrainerProfile setTrainer(TrainerCreateRequest request) {
        UUID userId = request.userId();
        ensureTrainerProfileNotExists(userId);

        User user = userUseCase.changeRole(userId, Role.TRAINER);
        TrainerSpecialization specialization = trainerSpecializationReader.getSpecializationById(request.specializationId());

        return createTrainerProfile(user, specialization);
    }

    private TrainerProfile createTrainerProfile(User user, TrainerSpecialization specialization) {
        TrainerProfile profile = new TrainerProfile();
        profile.setUser(user);
        profile.setSpecialization(specialization);
        return profile;
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('CLIENT', 'TRAINER', 'ADMIN', 'MANAGER')")
    public List<TrainerResponse> getTrainers() {
        return trainerRepository.findAllWithDetails()
                .stream()
                .map(trainerMapper::toResponse)
                .toList();
    }

    @PreAuthorize("hasAnyAuthority('CLIENT', 'TRAINER', 'ADMIN', 'MANAGER')")
    public TrainerResponse getTrainerById(UUID trainerId) {
        TrainerProfile entity = getTrainerProfile(trainerId);
        return trainerMapper.toResponse(entity);
    }

    @Override
    public TrainerProfile getTrainerProfile(UUID trainerId) {
        return trainerRepository.findById(trainerId).orElseThrow(
                () -> new NotFoundException("trainer.profile.not.found", trainerId)
        );
    }

    private void ensureTrainerProfileNotExists(UUID id) {
        if (trainerRepository.existsById(id)) {
            throw new ConflictException("trainer.profile.exists");
        }
    }
}
