package io.github.artsobol.fitnessclub.error;

public record ValidationFieldError(
        String field,
        String message
) {

}
