package io.github.artsobol.fitnessclub.feature.auth.serivce.api;

import io.github.artsobol.fitnessclub.feature.user.dto.User;

public interface AccessTokenService {

    String createAccessToken(User user);
}
