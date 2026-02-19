package io.github.artsobol.fitnessclub.feature.auth.serivce.api;

import io.github.artsobol.fitnessclub.feature.auth.dto.response.AuthResponse;
import io.github.artsobol.fitnessclub.feature.auth.dto.request.RegistrationRequest;

public interface RegistrationService {

    AuthResponse register(RegistrationRequest request);
}
