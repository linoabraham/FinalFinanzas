package com.travelers.proyectotravelers.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalErroHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> allExceptionHandler(Exception ex, WebRequest req) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .dateTime(LocalDateTime.now())
                .message(ex.getMessage())
                .path(req.getDescription(false))
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ModelNotFount.class)
    public ResponseEntity<?> modelNotFountException(ModelNotFount ex, WebRequest req) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .dateTime(LocalDateTime.now())
                .message(ex.getMessage())
                .path(req.getDescription(false))
                .statusCode(HttpStatus.NOT_FOUND.value())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<?> sqlException(SQLException ex, WebRequest req) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .dateTime(LocalDateTime.now())
                .message(ex.getMessage())
                .path(req.getDescription(false))
                .statusCode(HttpStatus.CONFLICT.value())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .dateTime(LocalDateTime.now())
                .message(ex.getMessage())
                .path(request.getDescription(false))
                .statusCode(HttpStatus.NOT_FOUND.value())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String message = ex.getFieldErrors()
                .stream()
                .map(e -> e.getField() + " ; " + e.getDefaultMessage())
                .collect(Collectors.joining(";"));
        ErrorResponse errorResponse = ErrorResponse.builder()
                .dateTime(LocalDateTime.now())
                .message(message)
                .path(request.getDescription(false))
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
