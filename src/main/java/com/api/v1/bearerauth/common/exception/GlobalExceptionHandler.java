package com.api.v1.bearerauth.common.exception;

import com.api.v1.bearerauth.common.response.ErrorResponse;
import com.api.v1.bearerauth.util.ApiResponseUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustom(CustomException ex, HttpServletRequest request) {
        ErrorResponse error = ex.getErrorResponse();
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(ex.getHttpStatus()).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        log.error("Unexpected error", e);
        ErrorResponse error = ApiResponseUtil.error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        ErrorResponse error = ApiResponseUtil.error(
                HttpStatus.BAD_REQUEST,
                "The request body is malformed. Please check the JSON format and try again.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.OK).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handlerArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        HashMap<String, String> errors = new HashMap<>();
        for (FieldError field : e.getBindingResult().getFieldErrors()) {
            errors.put(field.getField(), field.getDefaultMessage());
        }

        ErrorResponse errorResponse = ApiResponseUtil.error(
                HttpStatus.BAD_REQUEST,
                "Invalid Request",
                errors,
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.OK).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(HttpServletRequest request) {
        ErrorResponse errorResponse = ApiResponseUtil.error(
                HttpStatus.UNAUTHORIZED,
                "UNAUTHORIZED",
                "Access denied",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.OK).body(errorResponse);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(JwtException e, HttpServletRequest request) {
        ErrorResponse errorResponse = ApiResponseUtil.error(
                HttpStatus.UNAUTHORIZED,
                "Invalid token.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.OK).body(errorResponse);
    }
}