package io.github.artsobol.fitnessclub.feature.trainerspecialization.web;

import io.github.artsobol.fitnessclub.feature.trainerspecialization.dto.request.TrainerSpecializationCreateRequest;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.dto.response.TrainerSpecializationResponse;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.service.TrainerSpecializationUseCase;
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
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrainerSpecializationController.class)
@AutoConfigureMockMvc(addFilters = false)
class TrainerSpecializationControllerTest {

    @Autowired MockMvc mockMvc;
    @MockitoBean TrainerSpecializationUseCase useCase;
    @Autowired ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/trainers/specializations";

    @Test
    @DisplayName("Get by id: trainer specialization exists - returns specialization")
    void shouldReturnTrainerSpecialization_WhenTrainerSpecializationExistsById() throws Exception {
        // given
        Long id = 1L;
        TrainerSpecializationResponse response = new TrainerSpecializationResponse(id, "Trainer");
        when(useCase.getById(id)).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(get(BASE_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.title").value(response.title()));

        verify(useCase).getById(id);
        verifyNoMoreInteractions(useCase);
    }

    @Test
    @DisplayName("Get all: trainer specializations exist - returns list")
    void shouldReturnAllTrainerSpecializations_WhenSpecializationsExist() throws Exception {
        // given
        List<TrainerSpecializationResponse> response = List.of(
                new TrainerSpecializationResponse(1L, "Trainer"),
                new TrainerSpecializationResponse(2L, "Main trainer")
        );
        when(useCase.getAll()).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(get(BASE_URL)
                .accept(MediaType.APPLICATION_JSON));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Trainer"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Main trainer"));

        verify(useCase).getAll();
        verifyNoMoreInteractions(useCase);
    }

    @Test
    @DisplayName("Create: valid request - returns created trainer specialization")
    void shouldReturnTrainerSpecialization_WhenCreated() throws Exception {
        // given
        TrainerSpecializationCreateRequest request = new TrainerSpecializationCreateRequest("Trainer");
        TrainerSpecializationResponse response = new TrainerSpecializationResponse(1L, "Trainer");
        when(useCase.create(request)).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(post(BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsBytes(request)));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.title").value(response.title()));

        verify(useCase).create(request);
        verifyNoMoreInteractions(useCase);
    }

    @Test
    @DisplayName("Create: blank title - returns Bad Request")
    void shouldReturn400_WhenTitleIsBlank() throws Exception {
        // given
        TrainerSpecializationCreateRequest request = new TrainerSpecializationCreateRequest("");

        // when
        ResultActions result = mockMvc.perform(post(BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsBytes(request)));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(BASE_URL))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors.length()").value(1))
                .andExpect(jsonPath("$.errors[0].field").value("title"))
                .andExpect(jsonPath("$.errors[0].message").isNotEmpty());

        verifyNoInteractions(useCase);
    }
}