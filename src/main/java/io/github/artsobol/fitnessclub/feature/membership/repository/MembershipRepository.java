package io.github.artsobol.fitnessclub.feature.membership.repository;

import io.github.artsobol.fitnessclub.feature.membership.entity.Membership;
import io.github.artsobol.fitnessclub.feature.membership.entity.MembershipStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    List<Membership> findAllByUserId(UUID userId);

    Optional<Membership> findByUserIdAndStatus(UUID userId, MembershipStatus status);


}
