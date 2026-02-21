package io.github.artsobol.fitnessclub.feature.booking.web;

import io.github.artsobol.fitnessclub.feature.booking.dto.BookingResponse;
import io.github.artsobol.fitnessclub.feature.booking.service.ClientBookingUseCase;
import io.github.artsobol.fitnessclub.feature.testdata.booking.BookingResponseTestBuilder;
import io.github.artsobol.fitnessclub.feature.user.entity.Role;
import io.github.artsobol.fitnessclub.infrastructure.security.user.UserPrincipal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientBookingController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClientBookingControllerTest {

    @Autowired MockMvc mockMvc;
    @MockitoBean ClientBookingUseCase useCase;

    private static final String BASE_URL = "/api/me";
    private static final UUID USER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UserPrincipal PRINCIPAL = new UserPrincipal(USER_ID, "client", Role.CLIENT);

    @BeforeEach
    void setUp() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(PRINCIPAL, null, List.of()));
        SecurityContextHolder.setContext(context);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Get booking by workout id: client has booking for workout - returns booking")
    void shouldReturnClientBooking_whenClientHasBookingForWorkout() throws Exception {
        // given
        Long workoutId = 10L;
        BookingResponse response = BookingResponseTestBuilder.defaultResponse();
        when(useCase.getClientBooking(USER_ID, workoutId)).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(get(BASE_URL + "/workouts/" + workoutId + "/bookings")
                .accept(MediaType.APPLICATION_JSON));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Book workout: workout booked successfully - returns booking")
    void shouldReturnBooking_whenWorkoutBooked() throws Exception {
        // given
        Long workoutId = 10L;
        BookingResponse response = BookingResponseTestBuilder.defaultResponse();
        when(useCase.book(USER_ID, workoutId)).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(post(BASE_URL + "/workouts/" + workoutId + "/book")
                .accept(MediaType.APPLICATION_JSON));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Cancel booking: workout canceled successfully - returns booking")
    void shouldReturnBooking_whenWorkoutCanceled() throws Exception {
        // given
        Long workoutId = 10L;
        BookingResponse response = BookingResponseTestBuilder.defaultResponse();
        when(useCase.cancel(USER_ID, workoutId)).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(patch(BASE_URL + "/workouts/" + workoutId + "/cancel")
                .accept(MediaType.APPLICATION_JSON));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }
}