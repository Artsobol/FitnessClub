package io.github.artsobol.fitnessclub.exception.security;

import io.github.artsobol.fitnessclub.exception.base.BaseException;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class AuthenticationException extends BaseException {
    public AuthenticationException(String messageKey, Object... args) {
        super(messageKey, messageKey, HttpStatus.UNAUTHORIZED, Map.of(), null, args);
    }
}
