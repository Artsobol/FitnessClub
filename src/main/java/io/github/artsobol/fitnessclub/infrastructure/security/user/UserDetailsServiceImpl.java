package io.github.artsobol.fitnessclub.infrastructure.security.user;

import io.github.artsobol.fitnessclub.exception.security.AuthenticationException;
import io.github.artsobol.fitnessclub.feature.user.User;
import io.github.artsobol.fitnessclub.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @NonNull
    @Override
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new AuthenticationException("invalid.credentials")
        );
        return new UserDetailsImpl(user);
    }
}
