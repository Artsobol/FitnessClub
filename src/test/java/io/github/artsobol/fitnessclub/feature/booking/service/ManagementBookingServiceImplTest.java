package io.github.artsobol.fitnessclub.feature.booking.service;

import io.github.artsobol.fitnessclub.feature.booking.dto.BookingResponse;
import io.github.artsobol.fitnessclub.feature.booking.entity.Booking;
import io.github.artsobol.fitnessclub.feature.booking.mapper.BookingMapper;
import io.github.artsobol.fitnessclub.feature.booking.repository.BookingRepository;
import io.github.artsobol.fitnessclub.feature.testdata.booking.BookingResponseTestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManagementBookingServiceImplTest {

    @Mock BookingRepository repository;
    @Mock BookingCommandService commandService;
    @Mock BookingMapper mapper;

    @InjectMocks ManagementBookingServiceImpl service;

    @Test
    @DisplayName("Get booking by id: booking exists - returns booking response")
    void getById_bookingExists_returnsBookingResponse() {
        // given
        Long bookingId = 10L;

        Booking entity = new Booking();
        BookingResponse response = BookingResponseTestBuilder.defaultResponse();

        when(commandService.findByBookingId(bookingId)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(response);

        // when
        BookingResponse result = service.getById(bookingId);

        // then
        assertNotNull(result);
        assertEquals(response, result);

        verify(commandService).findByBookingId(bookingId);
        verify(mapper).toResponse(entity);

        verifyNoMoreInteractions(commandService, mapper);
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Get all bookings: bookings exist - returns list")
    void getAll_bookingsExist_returnsList() {
        // given
        Booking first = new Booking();
        Booking second = new Booking();

        BookingResponse firstResponse = BookingResponseTestBuilder.defaultResponse();
        BookingResponse secondResponse = BookingResponseTestBuilder.defaultResponse();

        when(repository.findAll()).thenReturn(List.of(first, second));
        when(mapper.toResponse(first)).thenReturn(firstResponse);
        when(mapper.toResponse(second)).thenReturn(secondResponse);

        // when
        List<BookingResponse> result = service.getAll();

        // then
        assertEquals(List.of(firstResponse, secondResponse), result);

        verify(repository).findAll();
        verify(mapper).toResponse(first);
        verify(mapper).toResponse(second);

        verifyNoMoreInteractions(repository, mapper);
        verifyNoInteractions(commandService);
    }

    @Test
    @DisplayName("Get bookings by workout id: bookings exist - returns list")
    void getAllByWorkoutId_bookingsExist_returnsList() {
        // given
        Long workoutId = 10L;

        Booking entity = new Booking();
        BookingResponse response = BookingResponseTestBuilder.defaultResponse();

        when(repository.findAllByWorkoutId(workoutId)).thenReturn(List.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        // when
        List<BookingResponse> result = service.getAllByWorkoutId(workoutId);

        // then
        assertEquals(List.of(response), result);

        verify(repository).findAllByWorkoutId(workoutId);
        verify(mapper).toResponse(entity);

        verifyNoMoreInteractions(repository, mapper);
        verifyNoInteractions(commandService);
    }

    @Test
    @DisplayName("Get bookings by user id: bookings exist - returns list")
    void getAllByUserId_bookingsExist_returnsList() {
        // given
        UUID userId = UUID.randomUUID();

        Booking entity = new Booking();
        BookingResponse response = BookingResponseTestBuilder.defaultResponse();

        when(repository.findAllByUserId(userId)).thenReturn(List.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        // when
        List<BookingResponse> result = service.getAllByUserId(userId);

        // then
        assertEquals(List.of(response), result);

        verify(repository).findAllByUserId(userId);
        verify(mapper).toResponse(entity);

        verifyNoMoreInteractions(repository, mapper);
        verifyNoInteractions(commandService);
    }

    @Test
    @DisplayName("Book workout for user: request is valid - returns booking response")
    void bookForUser_validRequest_returnsBookingResponse() {
        // given
        UUID userId = UUID.randomUUID();
        Long workoutId = 10L;

        Booking booking = new Booking();
        BookingResponse response = BookingResponseTestBuilder.defaultResponse();

        when(commandService.book(workoutId, userId)).thenReturn(booking);
        when(mapper.toResponse(booking)).thenReturn(response);

        // when
        BookingResponse result = service.bookForUser(userId, workoutId);

        // then
        assertNotNull(result);
        assertEquals(response, result);

        verify(commandService).book(workoutId, userId);
        verify(mapper).toResponse(booking);

        verifyNoMoreInteractions(commandService, mapper);
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Cancel workout for user: request is valid - returns booking response")
    void cancelForUser_validRequest_returnsBookingResponse() {
        // given
        UUID userId = UUID.randomUUID();
        Long workoutId = 10L;

        Booking booking = new Booking();
        BookingResponse response = BookingResponseTestBuilder.defaultResponse();

        when(commandService.cancel(workoutId, userId)).thenReturn(booking);
        when(mapper.toResponse(booking)).thenReturn(response);

        // when
        BookingResponse result = service.cancelForUser(userId, workoutId);

        // then
        assertNotNull(result);
        assertEquals(response, result);

        verify(commandService).cancel(workoutId, userId);
        verify(mapper).toResponse(booking);

        verifyNoMoreInteractions(commandService, mapper);
        verifyNoInteractions(repository);
    }
}