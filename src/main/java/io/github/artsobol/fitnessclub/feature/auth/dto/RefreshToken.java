package io.github.artsobol.fitnessclub.feature.auth.dto;

import io.github.artsobol.fitnessclub.feature.user.dto.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "refresh_tokens",
    indexes = {
        @Index(name = "idx_refresh_tokens_user_id", columnList = "user_id"),
            @Index(name = "idx_refresh_tokens_user_id_revoked", columnList = "user_id, revoked"),
            @Index(name = "idx_refresh_tokens_session_id", columnList = "session_id")
    }
)
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor()
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_refresh_tokens")
    @SequenceGenerator(name = "seq_refresh_tokens", sequenceName = "seq_refresh_tokens", allocationSize = 1)
    private Long id;

    @Column(name = "token_hash", nullable = false, unique = true)
    private String tokenHash;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_refresh_tokens_user_id"))
    private User user;

    @Column(name = "revoked", nullable = false)
    private boolean revoked = false;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @OneToOne
    @JoinColumn(name = "replace_by_id", unique = true)
    private RefreshToken replaceBy;

    @Column(name = "session_id", nullable = false)
    private UUID sessionId;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "last_used_at", nullable = false)
    private Instant lastUsedAt;

    @PrePersist
    void prePersist(){
        if (lastUsedAt == null) {
            lastUsedAt = Instant.now();
        }
    }
}
