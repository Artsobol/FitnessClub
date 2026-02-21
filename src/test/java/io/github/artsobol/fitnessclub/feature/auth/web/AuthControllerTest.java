package io.github.artsobol.fitnessclub.feature.auth.web;

import io.github.artsobol.fitnessclub.feature.auth.dto.request.LoginRequest;
import io.github.artsobol.fitnessclub.feature.auth.dto.request.RegistrationRequest;
import io.github.artsobol.fitnessclub.feature.auth.dto.response.AuthResponse;
import io.github.artsobol.fitnessclub.feature.auth.dto.response.UserInfo;
import io.github.artsobol.fitnessclub.feature.auth.serivce.api.LoginService;
import io.github.artsobol.fitnessclub.feature.auth.serivce.api.RefreshService;
import io.github.artsobol.fitnessclub.feature.auth.serivce.api.RegistrationService;
import io.github.artsobol.fitnessclub.feature.user.entity.Role;
import io.github.artsobol.fitnessclub.infrastructure.security.config.properties.CookieProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.servlet.http.Cookie;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean CookieProperties properties;
    @MockitoBean RegistrationService registrationService;
    @MockitoBean LoginService loginService;
    @MockitoBean RefreshService refreshService;

    private static final String BASE_URL = "/api/auth";
    private static final String COOKIE_NAME = "refresh_token";

    @Test
    @DisplayName("Register user: valid request - returns auth response")
    void shouldReturnAuthResponse_whenRegistered() throws Exception {
        // given
        RegistrationRequest request = new RegistrationRequest(
                "user@example.com", "password", "password", "John", "Doe", LocalDate.now().minusYears(20)
        );
        AuthResponse response = new AuthResponse("access-token", "refresh-token", new UserInfo(
                UUID.randomUUID(), "user@example.com", Role.CLIENT
        ));
        mockCookieProperties();
        when(registrationService.register(request)).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(post(BASE_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString(COOKIE_NAME + "=refresh-token")))
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.user.username").value("user@example.com"))
                .andExpect(jsonPath("$.user.role").value(Role.CLIENT.name()));

        verify(registrationService).register(request);
        verifyNoMoreInteractions(registrationService);
    }

    @Test
    @DisplayName("Login user: valid request - returns auth response")
    void shouldReturnAuthResponse_whenLoggedIn() throws Exception {
        // given
        LoginRequest request = new LoginRequest("user@example.com", "password");
        AuthResponse response = new AuthResponse("access-token", "refresh-token", new UserInfo(
                UUID.randomUUID(), "user@example.com", Role.CLIENT
        ));
        mockCookieProperties();
        when(loginService.login(request)).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(post(BASE_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString(COOKIE_NAME + "=refresh-token")))
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.user.username").value("user@example.com"))
                .andExpect(jsonPath("$.user.role").value(Role.CLIENT.name()));

        verify(loginService).login(request);
        verifyNoMoreInteractions(loginService);
    }

    @Test
    @DisplayName("Refresh token: valid request - returns auth response")
    void shouldReturnAuthResponse_whenRefreshed() throws Exception {
        // given
        AuthResponse response = new AuthResponse("access-token", "refresh-token", new UserInfo(
                UUID.randomUUID(), "user@example.com", Role.CLIENT
        ));
        mockCookieProperties();
        when(refreshService.refresh("refresh-token")).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(post(BASE_URL + "/refresh")
                .cookie(new Cookie(COOKIE_NAME, "refresh-token"))
                .accept(MediaType.APPLICATION_JSON));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString(COOKIE_NAME + "=refresh-token")))
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.user.username").value("user@example.com"))
                .andExpect(jsonPath("$.user.role").value(Role.CLIENT.name()));

        verify(refreshService).refresh("refresh-token");
        verifyNoMoreInteractions(refreshService);
    }

    @Test
    @DisplayName("Registration: invalid email - returns Bad Request")
    void shouldReturn400_whenRegistrationEmailIsBlank() throws Exception {
        // given
        RegistrationRequest request = new RegistrationRequest(
                null, "password", "password", "John", "Doe", LocalDate.now().minusYears(20)
        );

        // when
        ResultActions result = mockMvc.perform(post(BASE_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/register"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors.length()").value(1))
                .andExpect(jsonPath("$.errors[0].field").value("email"))
                .andExpect(jsonPath("$.errors[0].message").isNotEmpty());

        verifyNoInteractions(registrationService);
    }

    @Test
    @DisplayName("Login: invalid email - returns Bad Request")
    void shouldReturn400_whenLoginEmailIsInvalid() throws Exception {
        // given
        LoginRequest request = new LoginRequest("invalid-email", "password");

        // when
        ResultActions result = mockMvc.perform(post(BASE_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/login"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors.length()").value(1))
                .andExpect(jsonPath("$.errors[0].field").value("email"))
                .andExpect(jsonPath("$.errors[0].message").isNotEmpty());

        verifyNoInteractions(loginService);
    }

    private void mockCookieProperties() {
        when(properties.cookieName()).thenReturn(COOKIE_NAME);
        when(properties.secure()).thenReturn(false);
        when(properties.sameSite()).thenReturn("Lax");
        when(properties.path()).thenReturn("/");
        when(properties.maxAge()).thenReturn(Duration.ofDays(30));
    }
}