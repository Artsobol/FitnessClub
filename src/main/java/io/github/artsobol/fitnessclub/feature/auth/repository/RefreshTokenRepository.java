package io.github.artsobol.fitnessclub.feature.auth.repository;

import io.github.artsobol.fitnessclub.feature.auth.entity.RefreshToken;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    @Query("""
        select t
        from RefreshToken t
        where t.user.id = :userId
          and t.revokedAt is null
          and t.expiresAt > :now
        """)
    List<RefreshToken> findActiveByUserId(@Param("userId") UUID userId,
            @Param("now") Instant now);

    List<RefreshToken> findBySessionId(UUID sessionId);

    @Modifying
    @Query("""
        update RefreshToken t
           set t.revokedAt = :now
         where t.sessionId = :sessionId
           and t.revokedAt is null
        """)
    int revokeBySessionId(@Param("sessionId") UUID sessionId,
            @Param("now") Instant now);

    @Modifying
    @Query("""
        update RefreshToken t
           set t.revokedAt = :now
         where t.user.id = :userId
           and t.revokedAt is null
        """)
    int revokeAllByUserId(@Param("userId") UUID userId,
            @Param("now") Instant now);

    @Query("""
        select count(distinct t.sessionId)
        from RefreshToken t
        where t.user.id = :userId
          and t.revokedAt is null
          and t.expiresAt > :now
        """)
    int countActiveSessions(@Param("userId") UUID userId,
            @Param("now") Instant now);

    @Query("""
        select t.sessionId
        from RefreshToken t
        where t.user.id = :userId
          and t.revokedAt is null
          and t.expiresAt > :now
        group by t.sessionId
        order by min(t.createdAt) asc
        """)
    List<UUID> findOldestActiveSessionId(@Param("userId") UUID userId,
            @Param("now") Instant now,
            Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select t
        from RefreshToken t
        where t.tokenHash = :hash
        """)
    Optional<RefreshToken> findByTokenHashForUpdate(@Param("hash") String hash);
}
