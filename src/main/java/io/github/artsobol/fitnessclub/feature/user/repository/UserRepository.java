package io.github.artsobol.fitnessclub.feature.user.repository;

import io.github.artsobol.fitnessclub.feature.user.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
