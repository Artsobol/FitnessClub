package io.github.artsobol.fitnessclub.feature.membership.web;

import io.github.artsobol.fitnessclub.feature.membership.dto.request.MembershipUpdateRequest;
import io.github.artsobol.fitnessclub.feature.membership.dto.response.MembershipResponse;
import io.github.artsobol.fitnessclub.feature.membership.service.ManagementMembershipUseCase;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ManagementMembershipController.class)
@AutoConfigureMockMvc(addFilters = false)
class ManagementMembershipControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean ManagementMembershipUseCase useCase;

    private static final String BASE_URL = "/api/memberships";

    @Test
    @DisplayName("Get all memberships: memberships exist - returns list of memberships")
    void shouldReturnAllMemberships_whenHasMemberships() throws Exception {
        // given
        MembershipResponse response = MembershipResponseTestBuilder.defaultResponse();
        when(useCase.getAll()).thenReturn(List.of(response));

        // when
        ResultActions result = mockMvc.perform(get(BASE_URL).accept(MediaType.APPLICATION_JSON));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        assertMembership(result, "$[0]", response);

        verify(useCase).getAll();
        verifyNoMoreInteractions(useCase);
    }

    @Test
    @DisplayName("Get membership by id: membership exists - returns membership")
    void shouldReturnMembership_whenMembershipExists() throws Exception {
        // given
        Long membershipId = 10L;
        MembershipResponse response = MembershipResponseTestBuilder.defaultResponse();
        when(useCase.getById(membershipId)).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(get(BASE_URL + "/" + membershipId).accept(MediaType.APPLICATION_JSON));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
        assertMembership(result, "$", response);

        verify(useCase).getById(membershipId);
        verifyNoMoreInteractions(useCase);
    }

    @Test
    @DisplayName("Update membership: valid request - returns updated membership")
    void shouldReturnMembership_whenUpdated() throws Exception {
        // given
        Long membershipId = 10L;
        MembershipUpdateRequest request = new MembershipUpdateRequest(LocalDate.now(), LocalDate.now().plusDays(30));
        MembershipResponse response = MembershipResponseTestBuilder.defaultResponse();
        when(useCase.update(membershipId, request)).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(patch(BASE_URL + "/" + membershipId)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsBytes(request)));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());
        assertMembership(result, "$", response);

        verify(useCase).update(membershipId, request);
        verifyNoMoreInteractions(useCase);
    }

    @Test
    @DisplayName("Activate membership: valid request - returns activated membership")
    void shouldReturnMembership_whenActivated() throws Exception {
        // given
        Long membershipId = 10L;
        MembershipResponse response = MembershipResponseTestBuilder.defaultResponse();
        when(useCase.activate(membershipId)).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(patch(BASE_URL + "/" + membershipId + "/activate")
                .accept(MediaType.APPLICATION_JSON));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());
        assertMembership(result, "$", response);

        verify(useCase).activate(membershipId);
        verifyNoMoreInteractions(useCase);
    }

    private static void assertMembership(ResultActions result, String path, MembershipResponse response) throws Exception {
        result
                .andExpect(jsonPath(path + ".id").value(response.id()))
                .andExpect(jsonPath(path + ".status").value(response.status().name()))
                .andExpect(jsonPath(path + ".startsAt").value(response.startsAt().toString()))
                .andExpect(jsonPath(path + ".endsAt").value(response.endsAt().toString()))
                .andExpect(jsonPath(path + ".user.id").value(response.user().id().toString()))
                .andExpect(jsonPath(path + ".user.firstName").value(response.user().firstName()))
                .andExpect(jsonPath(path + ".user.lastName").value(response.user().lastName()));
    }
}
