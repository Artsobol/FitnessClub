package io.github.artsobol.fitnessclub.feature.user.service.api;

import io.github.artsobol.fitnessclub.feature.user.dto.User;
import io.github.artsobol.fitnessclub.feature.user.dto.UserCreateRequest;

public interface UserService {

    User createUser(UserCreateRequest request);

    User findUserByUsername(String username);
}
