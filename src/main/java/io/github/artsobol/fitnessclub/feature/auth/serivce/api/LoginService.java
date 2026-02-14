package io.github.artsobol.fitnessclub.feature.auth.serivce.api;

import io.github.artsobol.fitnessclub.feature.auth.dto.AuthResponse;
import io.github.artsobol.fitnessclub.feature.auth.dto.LoginRequest;

public interface LoginService {

    AuthResponse login(LoginRequest request);
}
