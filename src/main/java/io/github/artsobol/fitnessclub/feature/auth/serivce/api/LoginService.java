package io.github.artsobol.fitnessclub.feature.auth.serivce.api;

import io.github.artsobol.fitnessclub.feature.auth.dto.response.AuthResponse;
import io.github.artsobol.fitnessclub.feature.auth.dto.request.LoginRequest;

public interface LoginService {

    AuthResponse login(LoginRequest request);
}
