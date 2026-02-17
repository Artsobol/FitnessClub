package io.github.artsobol.fitnessclub.feature.user.service;

import io.github.artsobol.fitnessclub.feature.user.entity.User;

import java.util.UUID;

public interface UserService {

    User findById(UUID userId);
}
