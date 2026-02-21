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
class ClientBookingServiceImplTest {

    @Mock BookingRepository repository;
    @Mock BookingCommandService commandService;
    @Mock BookingMapper mapper;

    @InjectMocks ClientBookingServiceImpl service;

    @Test
    @DisplayName("Book workout: client books - returns booking response")
    void book_clientBooks_returnsBookingResponse() {
        // given
        UUID userId = UUID.randomUUID();
        Long workoutId = 10L;

        Booking booking = new Booking();
        BookingResponse response = BookingResponseTestBuilder.defaultResponse();

        when(commandService.book(workoutId, userId)).thenReturn(booking);
        when(mapper.toResponse(booking)).thenReturn(response);

        // when
        BookingResponse result = service.book(userId, workoutId);

        // then
        assertNotNull(result);
        assertEquals(response, result);

        verify(commandService).book(workoutId, userId);
        verify(mapper).toResponse(booking);

        verifyNoMoreInteractions(commandService, mapper);
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Cancel booking: client cancels - returns booking response")
    void cancel_clientCancels_returnsBookingResponse() {
        // given
        UUID userId = UUID.randomUUID();
        Long workoutId = 10L;

        Booking booking = new Booking();
        BookingResponse response = BookingResponseTestBuilder.defaultResponse();

        when(commandService.cancel(workoutId, userId)).thenReturn(booking);
        when(mapper.toResponse(booking)).thenReturn(response);

        // when
        BookingResponse result = service.cancel(userId, workoutId);

        // then
        assertNotNull(result);
        assertEquals(response, result);

        verify(commandService).cancel(workoutId, userId);
        verify(mapper).toResponse(booking);

        verifyNoMoreInteractions(commandService, mapper);
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Get client bookings: bookings exist - returns list")
    void getClientBookings_bookingsExist_returnsList() {
        // given
        UUID userId = UUID.randomUUID();

        Booking first = new Booking();
        Booking second = new Booking();

        BookingResponse firstResponse = BookingResponseTestBuilder.defaultResponse();
        BookingResponse secondResponse = BookingResponseTestBuilder.defaultResponse();

        when(repository.findByUserId(userId)).thenReturn(List.of(first, second));
        when(mapper.toResponse(first)).thenReturn(firstResponse);
        when(mapper.toResponse(second)).thenReturn(secondResponse);

        // when
        List<BookingResponse> result = service.getClientBookings(userId);

        // then
        assertEquals(List.of(firstResponse, secondResponse), result);

        verify(repository).findByUserId(userId);
        verify(mapper).toResponse(first);
        verify(mapper).toResponse(second);

        verifyNoMoreInteractions(repository, mapper);
        verifyNoInteractions(commandService);
    }

    @Test
    @DisplayName("Get client booking: booking exists - returns booking response")
    void getClientBooking_bookingExists_returnsBookingResponse() {
        // given
        UUID userId = UUID.randomUUID();
        Long workoutId = 10L;

        Booking booking = new Booking();
        BookingResponse response = BookingResponseTestBuilder.defaultResponse();

        when(commandService.findByWorkoutIdAndUserId(workoutId, userId)).thenReturn(booking);
        when(mapper.toResponse(booking)).thenReturn(response);

        // when
        BookingResponse result = service.getClientBooking(userId, workoutId);

        // then
        assertNotNull(result);
        assertEquals(response, result);

        verify(commandService).findByWorkoutIdAndUserId(workoutId, userId);
        verify(mapper).toResponse(booking);

        verifyNoMoreInteractions(commandService, mapper);
        verifyNoInteractions(repository);
    }
}