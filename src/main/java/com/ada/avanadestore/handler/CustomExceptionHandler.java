package com.ada.avanadestore.handler;

import com.ada.avanadestore.dto.StandardErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<StandardErrorDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        LOGGER.error("{}: {}", exception.getClass(), exception.getMessage());
        List<String> errorMessages = new ArrayList<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            String message = fieldError.getDefaultMessage();
            errorMessages.add(message);
        }

        StandardErrorDTO error = new StandardErrorDTO(HttpStatus.BAD_REQUEST.value(), String.join("; ", errorMessages), request.getRequestURI(), request.getMethod(), new Date().toString());
        return ResponseEntity.status(error.status()).body(error);
    }

    @ExceptionHandler
    public ResponseEntity<StandardErrorDTO> handleBadCredentialException(BadCredentialsException exception, HttpServletRequest request) {
        LOGGER.error("{}: {}", exception.getClass(), exception.getMessage());
        StandardErrorDTO error = new StandardErrorDTO(HttpStatus.FORBIDDEN.value(), exception.getMessage(), request.getRequestURI(), request.getMethod(), new Date().toString());
        return ResponseEntity.status(error.status()).body(error);
    }

    @ExceptionHandler
    public ResponseEntity<StandardErrorDTO> handleNullPointerException(NullPointerException exception, HttpServletRequest request) {
        LOGGER.error("{}: {}", exception.getClass(), exception.getMessage());
        StandardErrorDTO error = new StandardErrorDTO(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), request.getRequestURI(), request.getMethod(), new Date().toString());
        return ResponseEntity.status(error.status()).body(error);
    }
}
