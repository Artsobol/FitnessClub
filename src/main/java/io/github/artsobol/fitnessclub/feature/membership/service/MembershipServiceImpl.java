package io.github.artsobol.fitnessclub.feature.membership.service;

import io.github.artsobol.fitnessclub.exception.http.BadRequestException;
import io.github.artsobol.fitnessclub.exception.http.NotFoundException;
import io.github.artsobol.fitnessclub.feature.membership.dto.MembershipCreateRequest;
import io.github.artsobol.fitnessclub.feature.membership.dto.MembershipResponse;
import io.github.artsobol.fitnessclub.feature.membership.dto.MembershipUpdateRequest;
import io.github.artsobol.fitnessclub.feature.membership.entity.Membership;
import io.github.artsobol.fitnessclub.feature.membership.entity.MembershipStatus;
import io.github.artsobol.fitnessclub.feature.membership.mapper.MembershipMapper;
import io.github.artsobol.fitnessclub.feature.membership.repository.MembershipRepository;
import io.github.artsobol.fitnessclub.feature.user.entity.User;
import io.github.artsobol.fitnessclub.feature.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MembershipServiceImpl implements ManagementMembershipUseCase, ClientMembershipUseCase, StaffMembershipUseCase {

    private final MembershipRepository repository;
    private final UserService userService;
    private final MembershipMapper mapper;

    @Override
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public List<MembershipResponse> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public MembershipResponse getById(Long id) {
        return mapper.toResponse(findById(id));
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public MembershipResponse getMyActive(UUID userId){
        Membership entity = findActiveByUserId(userId);
        return mapper.toResponse(entity);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public List<MembershipResponse> getAllByUserId(UUID id) {
        return repository.findAllByUserId(id).stream().map(mapper::toResponse).toList();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public MembershipResponse getActiveByUserId(UUID userId) {
        Membership entity = findActiveByUserId(userId);
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public MembershipResponse create(UUID userId, MembershipCreateRequest request) {
        ensureCorrectDates(request.startsAt(), request.endsAt());

        Membership entity = mapper.toEntity(request);
        User user = userService.findById(userId);
        entity.setUser(user);
        entity.setPending();

        Membership saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public MembershipResponse update(Long membershipId, MembershipUpdateRequest request) {
        Membership entity = findById(membershipId);
        mapper.update(entity, request);
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public MembershipResponse activate(Long membershipId) {
        Membership entity = findById(membershipId);
        entity.setActive();
        return mapper.toResponse(entity);
    }

    protected Membership findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("membership.not.found"));
    }

    protected Membership findActiveByUserId(UUID userId) {
        return repository.findByUserIdAndStatus(userId, MembershipStatus.ACTIVE).orElseThrow(
                () -> new NotFoundException("membership.active.not.found")
        );
    }

    private void ensureCorrectDates(LocalDate from, LocalDate to) {
        if (from.isAfter(to) || from.isEqual(to)) {
            throw new BadRequestException("membership.dates.error");
        }
    }
}
