package com.ada.avanadestore.handler;

import com.ada.avanadestore.dto.StandardErrorDTO;
import com.ada.avanadestore.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardErrorDTO> handleInvalidMethodArguments(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errorMessages = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST, String.join("; ", errorMessages));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<StandardErrorDTO> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<StandardErrorDTO> handleNullPointerException(NullPointerException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardErrorDTO> handleUnreadableMessage(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardErrorDTO> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<StandardErrorDTO> buildErrorResponse(Exception ex, HttpServletRequest request, HttpStatus status) {
        return buildErrorResponse(ex, request, status, ex.getMessage());
    }

    private ResponseEntity<StandardErrorDTO> buildErrorResponse(Exception ex, HttpServletRequest request, HttpStatus status, String message) {
        LOGGER.error("{}: {}", ex.getClass().getName(), ex.getMessage());
        StandardErrorDTO error = new StandardErrorDTO(
                status.value(),
                message,
                request.getRequestURI(),
                request.getMethod(),
                new Date().toString()
        );
        return ResponseEntity.status(status).body(error);
    }
}
