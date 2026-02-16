package io.github.artsobol.fitnessclub.feature.auth.serivce.impl;

import io.github.artsobol.fitnessclub.exception.security.AuthenticationException;
import io.github.artsobol.fitnessclub.feature.auth.dto.EncodedRefreshToken;
import io.github.artsobol.fitnessclub.feature.auth.dto.RefreshToken;
import io.github.artsobol.fitnessclub.feature.auth.dto.RotatedRefresh;
import io.github.artsobol.fitnessclub.feature.auth.repository.RefreshTokenRepository;
import io.github.artsobol.fitnessclub.feature.auth.serivce.api.RefreshTokenService;
import io.github.artsobol.fitnessclub.feature.user.entity.User;
import io.github.artsobol.fitnessclub.infrastructure.security.config.properties.SessionProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final SessionProperties sessionProperties;
    private final RefreshTokenRepository repository;
    private final RefreshTokenEncoder encoder;

    @Override
    @Transactional
    public String createRefreshToken(User user, UUID sessionId) {
        Instant now = Instant.now();

        long activeSessions = repository.countActiveSessions(user.getId(), now);
        ensureHasSessions(user.getId(), activeSessions, now);

        EncodedRefreshToken encoded = encoder.create(user, sessionId);
        repository.save(encoded.refreshToken());
        return encoded.rawToken();
    }

    @Transactional
    public RotatedRefresh rotate(String rawRefreshToken) {
        Instant now = Instant.now();

        String hash = encoder.hash(rawRefreshToken);

        RefreshToken old = repository.findByTokenHashForUpdate(hash)
                .orElseThrow(() -> new AuthenticationException("auth.refresh.invalid"));

        if (old.getExpiresAt() != null && now.isAfter(old.getExpiresAt())) {
            throw new AuthenticationException("auth.refresh.expired");
        }

        if (old.isRevoked() || old.getRevokedAt() != null) {
            repository.revokeBySessionId(old.getSessionId(), now);
            throw new AuthenticationException("auth.refresh.reused");
        }

        old.setRevoked(true);
        old.setRevokedAt(now);
        old.setLastUsedAt(now);

        EncodedRefreshToken encoded = encoder.create(old.getUser(), old.getSessionId());
        RefreshToken newToken = encoded.refreshToken();

        repository.save(newToken);

        old.setReplaceBy(newToken);
        repository.save(old);

        return new RotatedRefresh(old.getUser(), encoded.rawToken());
    }

    private void ensureHasSessions(UUID userId, long activeSessions, Instant now) {
        if (activeSessions >= sessionProperties.maxSessions()) {
            repository.findOldestActiveSessionId(userId, now, PageRequest.of(0, 1))
                    .stream()
                    .findFirst()
                    .ifPresent(oldestSessionId -> repository.revokeBySessionId(oldestSessionId, now));
        }
    }
}

