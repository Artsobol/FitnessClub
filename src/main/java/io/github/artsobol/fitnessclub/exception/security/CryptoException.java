package io.github.artsobol.fitnessclub.exception.security;

import io.github.artsobol.fitnessclub.exception.base.BaseException;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class CryptoException extends BaseException {
    public CryptoException(String messageKey, Object... args) {
        super(messageKey, messageKey, HttpStatus.INTERNAL_SERVER_ERROR, Map.of(), null, args);
    }
}
