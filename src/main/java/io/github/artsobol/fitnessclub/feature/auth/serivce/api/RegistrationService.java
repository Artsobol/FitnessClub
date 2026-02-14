package io.github.artsobol.fitnessclub.feature.auth.serivce.api;

import io.github.artsobol.fitnessclub.feature.auth.dto.AuthResponse;
import io.github.artsobol.fitnessclub.feature.auth.dto.RegistrationRequest;

public interface RegistrationService {

    AuthResponse register(RegistrationRequest request);
}
