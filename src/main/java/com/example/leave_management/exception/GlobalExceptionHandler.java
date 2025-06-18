package com.example.leave_management.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.management.InvalidAttributeValueException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>("Invalid password or username", HttpStatus.UNAUTHORIZED);
    }

@ExceptionHandler(HttpMessageNotReadableException.class)
public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
    Throwable cause = ex.getCause();

    if (cause instanceof InvalidFormatException formatEx) {
        String fieldName = formatEx.getPath().get(0).getFieldName();
        String targetType = formatEx.getTargetType().getSimpleName();

        if (formatEx.getTargetType().equals(LocalDate.class)) {
            return new ResponseEntity<>(
                    "Invalid date format for field '" + fieldName + "'. Expected format: yyyy-MM-dd.",
                    HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity<>(
                "Invalid value for field '" + fieldName + "'. Expected type: " + targetType,
                HttpStatus.BAD_REQUEST
        );
    }

    return new ResponseEntity<>(
            "Malformed JSON request: " + ex.getMessage(),
            HttpStatus.BAD_REQUEST
    );
}
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<>("something went wrong"+ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
