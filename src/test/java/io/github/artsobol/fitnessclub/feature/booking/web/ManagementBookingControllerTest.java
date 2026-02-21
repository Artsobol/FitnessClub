package io.github.artsobol.fitnessclub.feature.booking.web;

import io.github.artsobol.fitnessclub.feature.booking.dto.BookingResponse;
import io.github.artsobol.fitnessclub.feature.booking.service.ManagementBookingUseCase;
import io.github.artsobol.fitnessclub.feature.testdata.booking.BookingResponseTestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ManagementBookingController.class)
@AutoConfigureMockMvc(addFilters = false)
class ManagementBookingControllerTest {

    @Autowired MockMvc mockMvc;
    @MockitoBean ManagementBookingUseCase useCase;

    private static final String BASE_URL = "/api";

    @Test
    @DisplayName("Get all bookings: bookings exist - returns list of bookings")
    void shouldReturnAllBookings_whenHasBookings() throws Exception {
        // given
        BookingResponse response = BookingResponseTestBuilder.defaultResponse();
        when(useCase.getAll()).thenReturn(List.of(response));

        // when
        ResultActions result = mockMvc.perform(get(BASE_URL + "/bookings").accept(MediaType.APPLICATION_JSON));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        assertBooking(result, "$[0]", response);

        verify(useCase).getAll();
        verifyNoMoreInteractions(useCase);
    }

    @Test
    @DisplayName("Get booking by id: booking exists - returns booking")
    void shouldReturnBooking_whenBookingExists() throws Exception {
        // given
        Long bookingId = 10L;
        BookingResponse response = BookingResponseTestBuilder.defaultResponse();
        when(useCase.getById(bookingId)).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(get(BASE_URL + "/bookings/" + bookingId)
                .accept(MediaType.APPLICATION_JSON));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
        assertBooking(result, "$", response);

        verify(useCase).getById(bookingId);
        verifyNoMoreInteractions(useCase);
    }

    @Test
    @DisplayName("Get bookings by workout id: bookings exist for workout - returns list")
    void shouldReturnBookings_whenWorkoutHasBookings() throws Exception {
        // given
        Long workoutId = 10L;
        BookingResponse response = BookingResponseTestBuilder.defaultResponse();
        when(useCase.getAllByWorkoutId(workoutId)).thenReturn(List.of(response));

        // when
        ResultActions result = mockMvc.perform(get(BASE_URL + "/workouts/" + workoutId + "/bookings")
                .accept(MediaType.APPLICATION_JSON));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        assertBooking(result, "$[0]", response);

        verify(useCase).getAllByWorkoutId(workoutId);
        verifyNoMoreInteractions(useCase);
    }

    @Test
    @DisplayName("Get bookings by user id: bookings exist for user - returns list")
    void shouldReturnBookings_whenUserHasBookings() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        BookingResponse response = BookingResponseTestBuilder.defaultResponse();
        when(useCase.getAllByUserId(userId)).thenReturn(List.of(response));

        // when
        ResultActions result = mockMvc.perform(get(BASE_URL + "/users/" + userId + "/bookings")
                .accept(MediaType.APPLICATION_JSON));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        assertBooking(result, "$[0]", response);

        verify(useCase).getAllByUserId(userId);
        verifyNoMoreInteractions(useCase);
    }

    @Test
    @DisplayName("Book workout: workout booked successfully - returns booking")
    void shouldReturnBooking_whenBookedForUser() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        Long workoutId = 10L;
        BookingResponse response = BookingResponseTestBuilder.defaultResponse();
        when(useCase.bookForUser(userId, workoutId)).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(post(BASE_URL + "/users/" + userId + "/workouts/" + workoutId + "/book")
                .accept(MediaType.APPLICATION_JSON));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());
        assertBooking(result, "$", response);

        verify(useCase).bookForUser(userId, workoutId);
        verifyNoMoreInteractions(useCase);
    }

    @Test
    @DisplayName("Cancel booking: booking canceled successfully - returns booking")
    void shouldReturnBooking_whenCancelledForUser() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        Long workoutId = 10L;
        BookingResponse response = BookingResponseTestBuilder.defaultResponse();
        when(useCase.cancelForUser(userId, workoutId)).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(patch(BASE_URL + "/users/" + userId + "/workouts/" + workoutId + "/book/cancel")
                .accept(MediaType.APPLICATION_JSON));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
        assertBooking(result, "$", response);

        verify(useCase).cancelForUser(userId, workoutId);
        verifyNoMoreInteractions(useCase);
    }

    private static void assertBooking(ResultActions result, String path, BookingResponse response) throws Exception {
        result
                .andExpect(jsonPath(path + ".id").value(response.id()))
                .andExpect(jsonPath(path + ".status").value(response.status().name()))
                .andExpect(jsonPath(path + ".workout.id").value(response.workout().id()))
                .andExpect(jsonPath(path + ".workout.title").value(response.workout().title()))
                .andExpect(jsonPath(path + ".workout.startsAt").value(response.workout().startsAt().toString()))
                .andExpect(jsonPath(path + ".workout.endsAt").value(response.workout().endsAt().toString()))
                .andExpect(jsonPath(path + ".user.id").value(response.user().id().toString()))
                .andExpect(jsonPath(path + ".user.firstName").value(response.user().firstName()))
                .andExpect(jsonPath(path + ".user.lastName").value(response.user().lastName()));
    }
}