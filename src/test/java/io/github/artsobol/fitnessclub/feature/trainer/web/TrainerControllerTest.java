package io.github.artsobol.fitnessclub.feature.trainer.web;

import io.github.artsobol.fitnessclub.exception.http.NotFoundException;
import io.github.artsobol.fitnessclub.feature.trainer.dto.TrainerCreateRequest;
import io.github.artsobol.fitnessclub.feature.trainer.dto.TrainerResponse;
import io.github.artsobol.fitnessclub.feature.trainer.service.TrainerUseCase;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.dto.response.TrainerSpecializationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrainerController.class)
class TrainerControllerTest {

    @Autowired MockMvc mockMvc;
    @MockitoBean TrainerUseCase useCase;
    @Autowired ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/trainers";

    @Test
    @DisplayName("Create trainer: valid request - returns created trainer")
    void shouldReturnTrainer_whenCreated() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        TrainerSpecializationResponse trainerSpecializationResponse = new TrainerSpecializationResponse(1L, "Trainer");
        TrainerResponse response = new TrainerResponse(id, "John", "Deer", trainerSpecializationResponse);
        TrainerCreateRequest request = new TrainerCreateRequest(id, 1L);
        when(useCase.createTrainer(request)).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsBytes(request)));

        // then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Deer"))
                .andExpect(jsonPath("$.specialization.id").value(1))
                .andExpect(jsonPath("$.specialization.title").value("Trainer"));

        verify(useCase).createTrainer(request);
        verifyNoMoreInteractions(useCase);
    }

    @Test
    @DisplayName("Get trainer by id: trainer exists - returns trainer")
    void shouldReturnTrainer_whenExistsById() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        TrainerSpecializationResponse trainerSpecializationResponse = new TrainerSpecializationResponse(1L, "Trainer");
        TrainerResponse response = new TrainerResponse(id, "John", "Deer", trainerSpecializationResponse);
        when(useCase.getTrainerById(id)).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(get(BASE_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON));

        // then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Deer"))
                .andExpect(jsonPath("$.specialization.id").value(1))
                .andExpect(jsonPath("$.specialization.title").value("Trainer"));

        verify(useCase).getTrainerById(id);
        verifyNoMoreInteractions(useCase);
    }

    @Test
    @DisplayName("Get trainer by id: trainer not found - returns 404")
    void shouldReturn404_whenTrainerNotFound() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        when(useCase.getTrainerById(id)).thenThrow(new NotFoundException("trainer.profile.not.found", id));

        // when
        ResultActions result = mockMvc.perform(get(BASE_URL + "/" + id)
                .accept(MediaType.APPLICATION_JSON));

        // then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.errorCode").value("trainer.profile.not.found"))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/" + id))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(useCase).getTrainerById(id);
        verifyNoMoreInteractions(useCase);
    }

    @Test
    @DisplayName("Get all trainers: trainers exist - returns list")
    void shouldReturnAllTrainers_WhenTrainersExist() throws Exception {
        // given
        UUID firstId = UUID.randomUUID();
        UUID secondId = UUID.randomUUID();
        TrainerSpecializationResponse trainerSpecializationResponse = new TrainerSpecializationResponse(1L, "Trainer");
        TrainerResponse firstTrainer = new TrainerResponse(firstId, "John", "Deer", trainerSpecializationResponse);
        TrainerResponse secondTrainer = new TrainerResponse(secondId, "Mike", "Vize", trainerSpecializationResponse);
        List<TrainerResponse> response = List.of(firstTrainer, secondTrainer);
        when(useCase.getTrainers()).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(get(BASE_URL)
                .accept(MediaType.APPLICATION_JSON));

        // then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(firstId.toString()))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Deer"))
                .andExpect(jsonPath("$[0].specialization.id").value(1))
                .andExpect(jsonPath("$[0].specialization.title").value("Trainer"))
                .andExpect(jsonPath("$[1].id").value(secondId.toString()))
                .andExpect(jsonPath("$[1].firstName").value("Mike"))
                .andExpect(jsonPath("$[1].lastName").value("Vize"))
                .andExpect(jsonPath("$[1].specialization.id").value(1))
                .andExpect(jsonPath("$[1].specialization.title").value("Trainer"));

        verify(useCase).getTrainers();
        verifyNoMoreInteractions(useCase);
    }
}