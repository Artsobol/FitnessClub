package io.github.artsobol.fitnessclub.infrastructure.security.jwt;

import io.github.artsobol.fitnessclub.infrastructure.security.config.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final Duration accessTokenExpiration;

    public JwtTokenProvider(JwtProperties properties) {
        byte[] ketBytes = Decoders.BASE64.decode(properties.secret());
        this.secretKey = Keys.hmacShaKeyFor(ketBytes);
        this.accessTokenExpiration = properties.accessTokenExpiration();
    }

    public String generateToken(JwtSubject subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpiration.toMillis());

        return Jwts.builder()
                .subject(subject.userId().toString())
                .claim("role", subject.role().name())
                .claim("username", subject.username())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public UUID extractUserId(String token) {
        Claims claims = parseToken(token);
        return UUID.fromString(claims.getSubject());
    }
}
