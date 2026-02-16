package io.github.artsobol.fitnessclub.feature.user.service.api;

import io.github.artsobol.fitnessclub.feature.user.entity.Role;
import io.github.artsobol.fitnessclub.feature.user.entity.User;
import io.github.artsobol.fitnessclub.feature.user.dto.UserCreateRequest;

import java.util.UUID;

public interface UserService {

    User createUser(UserCreateRequest request);

    User findUserByUsername(String username);

    User changeRole(UUID userId, Role role);
}
