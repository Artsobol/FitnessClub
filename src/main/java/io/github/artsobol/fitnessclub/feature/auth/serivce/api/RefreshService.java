package io.github.artsobol.fitnessclub.feature.auth.serivce.api;

import io.github.artsobol.fitnessclub.feature.auth.dto.AuthResponse;

public interface RefreshService {

    AuthResponse refresh(String refreshToken);
}
