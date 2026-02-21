package io.github.artsobol.fitnessclub.infrastructure.security.jwt;

import io.github.artsobol.fitnessclub.exception.http.BadRequestException;
import io.github.artsobol.fitnessclub.feature.user.entity.Role;
import io.github.artsobol.fitnessclub.infrastructure.security.user.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (!checkHeader(header)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            Claims claims = parseToken(header);
            UserPrincipal principal = createUserPrincipal(claims);
            createAuthentication(principal, getAuthority(claims));
            filterChain.doFilter(request, response);
        }

    }

    private UserPrincipal createUserPrincipal(Claims claims){
        UUID userId = UUID.fromString(claims.getSubject());
        String username = claims.get("username", String.class);
        String role = claims.get("role", String.class);

        return new UserPrincipal(userId, username, Role.valueOf(role));
    }

    private void createAuthentication(UserPrincipal principal, List<SimpleGrantedAuthority> authorities) {
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private Claims parseToken(String header) {
        try {
            String token = header.substring(7);
            return jwtTokenProvider.parseToken(token);
        } catch (JwtException e) {
            throw new BadRequestException("token.invalid");
        }
    }

    private List<SimpleGrantedAuthority> getAuthority(Claims claims) {
        String role = claims.get("role", String.class);
        ensureRoleExists(role);
        return List.of(new SimpleGrantedAuthority(role));
    }

    private void ensureRoleExists(String role){
        if (role == null|| role.isBlank()) {
            throw new BadRequestException("token.role.missing");
        }
    }

    private boolean checkHeader(String header) {
        return header != null && header.startsWith("Bearer ");
    }
}
