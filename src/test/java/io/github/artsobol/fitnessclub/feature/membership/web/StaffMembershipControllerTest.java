package io.github.artsobol.fitnessclub.feature.membership.web;

import io.github.artsobol.fitnessclub.feature.membership.dto.request.MembershipCreateRequest;
import io.github.artsobol.fitnessclub.feature.membership.dto.response.MembershipResponse;
import io.github.artsobol.fitnessclub.feature.membership.service.StaffMembershipUseCase;
import io.github.artsobol.fitnessclub.feature.testdata.membership.MembershipResponseTestBuilder;
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
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StaffMembershipController.class)
@AutoConfigureMockMvc(addFilters = false)
class StaffMembershipControllerTest {

    @Autowired MockMvc mockMvc;

    @Autowired ObjectMapper objectMapper;

    @MockitoBean StaffMembershipUseCase useCase;

    private static final String BASE_URL = "/api/users";

    @Test
    @DisplayName("Return memberships for user")
    void shouldReturnMemberships_whenUserHasMemberships() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        MembershipResponse response = MembershipResponseTestBuilder.defaultResponse();
        when(useCase.getAllByUserId(userId)).thenReturn(List.of(response));

        // when
        ResultActions result = mockMvc.perform(get(BASE_URL + "/" + userId + "/memberships")
                .accept(MediaType.APPLICATION_JSON));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        assertMembership(result, "$[0]", response);

        verify(useCase).getAllByUserId(userId);
    }

    @Test
    @DisplayName("Return active membership for user")
    void shouldReturnActiveMembership_whenUserHasActiveMembership() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        MembershipResponse response = MembershipResponseTestBuilder.defaultResponse();
        when(useCase.getActiveByUserId(userId)).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(get(BASE_URL + "/" + userId + "/memberships/active")
                .accept(MediaType.APPLICATION_JSON));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
        assertMembership(result, "$", response);

        verify(useCase).getActiveByUserId(userId);
    }

    @Test
    @DisplayName("Return membership after creation")
    void shouldReturnMembership_whenCreated() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        MembershipCreateRequest request = new MembershipCreateRequest(LocalDate.now(), LocalDate.now().plusDays(30));
        MembershipResponse response = MembershipResponseTestBuilder.defaultResponse();
        when(useCase.create(userId, request)).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(post(BASE_URL + "/" + userId + "/memberships")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsBytes(request)));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());
        assertMembership(result, "$", response);

        verify(useCase).create(userId, request);
    }

    @Test
    @DisplayName("Return Bad Request when startsAt is null")
    void shouldReturn400_whenStartsAtIsNull() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        MembershipCreateRequest request = new MembershipCreateRequest(null, LocalDate.now().plusDays(30));

        // when
        ResultActions result = mockMvc.perform(post(BASE_URL + "/" + userId + "/memberships")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsBytes(request)));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/" + userId + "/memberships"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors.length()").value(1))
                .andExpect(jsonPath("$.errors[0].field").value("startsAt"))
                .andExpect(jsonPath("$.errors[0].message").isNotEmpty());

        verifyNoInteractions(useCase);
    }

    private static void assertMembership(ResultActions result, String path, MembershipResponse response) throws Exception {
        result
                .andExpect(jsonPath(path + ".id").value(response.id()))
                .andExpect(jsonPath(path + ".status").value(response.status().name()))
                .andExpect(jsonPath(path + ".startsAt").value(response.startsAt().toString()))
                .andExpect(jsonPath(path + ".endsAt").value(response.endsAt().toString()))
                .andExpect(jsonPath(path + ".user.id").value(response.user().id().toString()))
                .andExpect(jsonPath(path + ".user.firstName").value(response.user().firstName()));
    }
}
