package io.github.artsobol.fitnessclub.feature.booking.service;

import io.github.artsobol.fitnessclub.feature.booking.dto.BookingResponse;
import io.github.artsobol.fitnessclub.feature.booking.mapper.BookingMapper;
import io.github.artsobol.fitnessclub.feature.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientBookingServiceImpl implements ClientBookingUseCase {

    private final BookingRepository repository;
    private final BookingCommandService commandService;
    private final BookingMapper mapper;

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('CLIENT')")
    public BookingResponse book(UUID userId, Long workoutId) {
        return mapper.toResponse(commandService.book(workoutId, userId));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('CLIENT')")
    public BookingResponse cancel(UUID userId, Long workoutId) {
        return mapper.toResponse(commandService.cancel(workoutId, userId));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('CLIENT')")
    public List<BookingResponse> getClientBookings(UUID userId) {
        return repository.findByUserId(userId).stream().map(mapper::toResponse).toList();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('CLIENT')")
    public BookingResponse getClientBooking(UUID userId, Long workoutId) {
        return mapper.toResponse(commandService.findByWorkoutIdAndUserId(workoutId, userId));
    }

}
