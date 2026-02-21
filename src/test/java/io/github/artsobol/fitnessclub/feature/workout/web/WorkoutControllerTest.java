package io.github.artsobol.fitnessclub.feature.workout.web;

import io.github.artsobol.fitnessclub.feature.testdata.workout.WorkoutRequestTestBuilder;
import io.github.artsobol.fitnessclub.feature.testdata.workout.WorkoutResponseTestBuilder;
import io.github.artsobol.fitnessclub.feature.user.entity.Role;
import io.github.artsobol.fitnessclub.feature.workout.dto.request.WorkoutRequest;
import io.github.artsobol.fitnessclub.feature.workout.dto.request.WorkoutUpdateRequest;
import io.github.artsobol.fitnessclub.feature.workout.dto.response.WorkoutResponse;
import io.github.artsobol.fitnessclub.feature.workout.service.WorkoutUseCase;
import io.github.artsobol.fitnessclub.infrastructure.security.user.UserPrincipal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
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
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.UUID;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(WorkoutController.class)
@AutoConfigureMockMvc(addFilters = false)
class WorkoutControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockitoBean WorkoutUseCase useCase;

    private static final String BASE_URL = "/api/workouts";
    private static final UUID USER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UserPrincipal PRINCIPAL = new UserPrincipal(USER_ID, "trainer", Role.TRAINER);

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
    @DisplayName("Create workout: title is blank - returns Bad Request")
    void createWorkout_blankTitle_returnsBadRequest() throws Exception {
        // given
        WorkoutRequest request = new WorkoutRequest("", "Description", Instant.now(), Instant.now().plusSeconds(60), 10);

        // when
        ResultActions result = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

        // then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(BASE_URL))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors.length()").value(1))
                .andExpect(jsonPath("$.errors[0].field").isNotEmpty())
                .andExpect(jsonPath("$.errors[0].message").isNotEmpty());

        verifyNoInteractions(useCase);
    }
}