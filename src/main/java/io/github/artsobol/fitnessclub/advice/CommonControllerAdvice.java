package io.github.artsobol.fitnessclub.advice;

import io.github.artsobol.fitnessclub.error.ErrorResponse;
import io.github.artsobol.fitnessclub.error.ValidationErrorResponse;
import io.github.artsobol.fitnessclub.error.ValidationFieldError;
import io.github.artsobol.fitnessclub.exception.base.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
public class CommonControllerAdvice {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        List<ValidationFieldError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ValidationFieldError(err.getField(), resolveValidationMessage(err)))
                .toList();

        String message = createMessage("validation.error", null);

        ValidationErrorResponse response = new ValidationErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                errors
        );

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex, HttpServletRequest request) {
        HttpStatus status = ex.getStatus();
        String message = createMessage(ex.getMessageKey(), ex.getMessageArgs());

        ErrorResponse response = new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getErrorCode(),
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = createMessage("unexpected.error", null);

        ErrorResponse response = new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                "INTERNAL_SERVER_ERROR",
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(response);
    }

    private String resolveValidationMessage(org.springframework.validation.FieldError err) {
        String defaultMessage = err.getDefaultMessage();
        if (defaultMessage == null || defaultMessage.isBlank()) {
            return "Validation error";
        }

        try {
            return messageSource.getMessage(defaultMessage, err.getArguments(), LocaleContextHolder.getLocale());
        } catch (Exception ignored) {
            return defaultMessage;
        }
    }

    private String createMessage(String key, Object[] args){
        try {
            Locale locale = LocaleContextHolder.getLocale();
            return messageSource.getMessage(key, args, locale);
        } catch (Exception e) {
            return "Enexpected error";
        }
    }
}
