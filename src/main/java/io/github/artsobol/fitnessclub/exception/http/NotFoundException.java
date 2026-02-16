package io.github.artsobol.fitnessclub.exception.http;

import io.github.artsobol.fitnessclub.exception.base.BaseException;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class NotFoundException extends BaseException {
    public NotFoundException(String messageKey, Object... args) {
        super(messageKey, messageKey, HttpStatus.NOT_FOUND, Map.of(), null, args);
    }
}
