package io.github.artsobol.fitnessclub.feature.trainerspecialization.service;

import io.github.artsobol.fitnessclub.exception.http.ConflictException;
import io.github.artsobol.fitnessclub.exception.http.NotFoundException;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.dto.request.TrainerSpecializationCreateRequest;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.dto.response.TrainerSpecializationResponse;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.dto.request.TrainerSpecializationUpdateRequest;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.entity.TrainerSpecialization;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.mapper.TrainerSpecializationMapper;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.repository.TrainerSpecializationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainerSpecializationServiceImpl implements TrainerSpecializationUseCase, TrainerSpecializationReader {

    private final TrainerSpecializationRepository repository;
    private final TrainerSpecializationMapper mapper;

    @Override
    @PreAuthorize("hasAnyAuthority('CLIENT', 'TRAINER', 'MANAGER', 'ADMIN')")
    public List<TrainerSpecializationResponse> getAll() {
        return repository.findAllByActiveTrue()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('CLIENT', 'TRAINER', 'MANAGER', 'ADMIN')")
    public TrainerSpecializationResponse getById(Long id) {
        return mapper.toResponse(getSpecializationById(id));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public TrainerSpecializationResponse create(TrainerSpecializationCreateRequest request) {
        ensureSpecializationNotExists(request.title());

        TrainerSpecialization entity = mapper.toEntity(request);
        TrainerSpecialization saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public TrainerSpecializationResponse update(Long id, TrainerSpecializationUpdateRequest request) {
        String newSpecialization = request.title();
        if (newSpecialization != null) {
            ensureSpecializationNotExists(request.title());
        }

        TrainerSpecialization entity = getSpecializationById(id);
        mapper.update(entity, request);
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public TrainerSpecializationResponse deactive(Long id) {
        TrainerSpecialization entity = getSpecializationById(id);
        entity.deactivate();

        return mapper.toResponse(entity);
    }

    public TrainerSpecialization getSpecializationById(Long id) {
        return repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("trainer.specialization.not.found"));
    }

    private void ensureSpecializationNotExists(String specialization) {
        if (repository.existsByTitleAndActiveTrue(specialization)) {
            throw new ConflictException("trainer.specialization.already.exists", specialization);
        }
    }
}
