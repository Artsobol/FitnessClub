package io.github.artsobol.fitnessclub.feature.booking.service;

import io.github.artsobol.fitnessclub.feature.booking.dto.BookingResponse;
import io.github.artsobol.fitnessclub.feature.booking.entity.Booking;
import io.github.artsobol.fitnessclub.feature.booking.mapper.BookingMapper;
import io.github.artsobol.fitnessclub.feature.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
public class ManagementBookingServiceImpl implements ManagementBookingUseCase {

    private final BookingRepository repository;
    private final BookingCommandService commandService;
    private final BookingMapper mapper;

    @Override
    public BookingResponse getById(Long bookingId) {
        Booking entity = commandService.findByBookingId(bookingId);
        return mapper.toResponse(entity);
    }

    @Override
    public List<BookingResponse> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public List<BookingResponse> getAllByWorkoutId(Long workoutId) {
        return  repository.findAllByWorkoutId(workoutId).stream().map(mapper::toResponse).toList();
    }

    @Override
    public List<BookingResponse> getAllByUserId(UUID userId) {
        return repository.findAllByUserId(userId).stream().map(mapper::toResponse).toList();
    }

    @Override
    public BookingResponse bookForUser(UUID userId, Long workoutId) {
        return mapper.toResponse(commandService.book(workoutId, userId));
    }

    @Override
    public BookingResponse cancelForUser(UUID userId, Long workoutId) {
        return mapper.toResponse(commandService.cancel(workoutId, userId));
    }


}
