package io.github.artsobol.fitnessclub.error;

import java.time.Instant;
import java.util.List;

public record ValidationErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        List<ValidationFieldError> errors
) {
}
