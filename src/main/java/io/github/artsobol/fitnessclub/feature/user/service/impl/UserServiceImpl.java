package io.github.artsobol.fitnessclub.feature.user.service.impl;

import io.github.artsobol.fitnessclub.exception.security.AuthenticationException;
import io.github.artsobol.fitnessclub.feature.user.mapper.UserMapper;
import io.github.artsobol.fitnessclub.feature.user.dto.Role;
import io.github.artsobol.fitnessclub.feature.user.dto.User;
import io.github.artsobol.fitnessclub.feature.user.dto.UserCreateRequest;
import io.github.artsobol.fitnessclub.feature.user.repository.UserRepository;
import io.github.artsobol.fitnessclub.feature.user.service.api.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    @Transactional
    public User createUser(UserCreateRequest request) {
        User user = mapper.toEntity(request);
        user.setRole(Role.CLIENT);
        return repository.save(user);
    }

    @Override
    public User findUserByUsername(String username) {
        return repository.findByEmail(username).orElseThrow(
                () ->  new AuthenticationException("auth.bad-credentials")
        );
    }
}
