package io.github.artsobol.fitnessclub.feature.auth.serivce.impl;

import io.github.artsobol.fitnessclub.feature.auth.dto.response.AuthResponse;
import io.github.artsobol.fitnessclub.feature.auth.dto.response.RotatedRefresh;
import io.github.artsobol.fitnessclub.feature.auth.dto.response.UserInfo;
import io.github.artsobol.fitnessclub.feature.auth.serivce.api.RefreshTokenService;
import io.github.artsobol.fitnessclub.feature.user.entity.Role;
import io.github.artsobol.fitnessclub.feature.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshServiceImplTest {

    @Mock RefreshTokenService refreshTokenService;
    @Mock AuthResponseFactory authResponseFactory;

    @InjectMocks RefreshServiceImpl service;

    @Test
    @DisplayName("Refresh: refresh is rotated - returns auth response")
    void refresh_refreshRotated_returnsAuthResponse() {
        // given
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("user@example.com");
        user.setRole(Role.CLIENT);

        RotatedRefresh rotated = new RotatedRefresh(user, "refresh");
        AuthResponse expectedResponse = new AuthResponse(
                "access",
                "refresh",
                new UserInfo(user.getId(), user.getEmail(), user.getRole())
        );

        when(refreshTokenService.rotate("refresh")).thenReturn(rotated);
        when(authResponseFactory.createWithRefresh(user, "refresh")).thenReturn(expectedResponse);

        // when
        AuthResponse result = service.refresh("refresh");

        // then
        assertNotNull(result);
        assertEquals(expectedResponse, result);

        // verify interactions
        verify(refreshTokenService).rotate("refresh");
        verify(authResponseFactory).createWithRefresh(user, "refresh");

        // verify no more interactions
        verifyNoMoreInteractions(refreshTokenService, authResponseFactory);
    }
}