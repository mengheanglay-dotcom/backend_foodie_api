package com.api.v1.bearerauth.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum MessageConstant {

    BAD_REQUEST("BAD_REQUEST", HttpStatus.BAD_REQUEST, "Invalid request"),
    VALIDATION_FAILED("VALIDATION_FAILED", HttpStatus.BAD_REQUEST, "Validation failed"),
    INVALID_INPUT("INVALID_INPUT", HttpStatus.BAD_REQUEST, "Invalid input provided"),
    MALFORMED_JSON("MALFORMED_JSON", HttpStatus.BAD_REQUEST, "Malformed JSON request"),
    MISSING_PARAMETER("MISSING_PARAMETER", HttpStatus.BAD_REQUEST, "Required parameter is missing"),
    INVALID_PARAMETER("INVALID_PARAMETER", HttpStatus.BAD_REQUEST, "Invalid parameter value"),
    MISSING_HEADER("MISSING_HEADER", HttpStatus.BAD_REQUEST, "Required header is missing"),

    UNAUTHORIZED("UNAUTHORIZED", HttpStatus.UNAUTHORIZED, "Authentication required"),
    INVALID_CREDENTIALS("INVALID_CREDENTIALS", HttpStatus.UNAUTHORIZED, "Invalid credentials"),
    INVALID_REFRESH_TOKEN("INVALID_REFRESH_TOKEN", HttpStatus.UNAUTHORIZED, "Invalid refresh token"),
    TOKEN_EXPIRED("TOKEN_EXPIRED", HttpStatus.UNAUTHORIZED, "Token has expired"),
    TOKEN_INVALID("TOKEN_INVALID", HttpStatus.UNAUTHORIZED, "Invalid token"),
    TOKEN_MISSING("TOKEN_MISSING", HttpStatus.UNAUTHORIZED, "Authentication token is missing"),

    FORBIDDEN("FORBIDDEN", HttpStatus.FORBIDDEN, "Access denied"),
    INSUFFICIENT_PERMISSIONS("INSUFFICIENT_PERMISSIONS", HttpStatus.FORBIDDEN, "Insufficient permissions"),
    ACCOUNT_LOCKED("ACCOUNT_LOCKED", HttpStatus.FORBIDDEN, "Account is locked"),
    ACCOUNT_DISABLED("ACCOUNT_DISABLED", HttpStatus.FORBIDDEN, "Account is disabled"),

    NOT_FOUND("NOT_FOUND", HttpStatus.NOT_FOUND, "Resource not found"),
    USER_NOT_FOUND("USER_NOT_FOUND", HttpStatus.NOT_FOUND, "User not found"),
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", HttpStatus.NOT_FOUND, "Resource not found"),
    ENDPOINT_NOT_FOUND("ENDPOINT_NOT_FOUND", HttpStatus.NOT_FOUND, "Endpoint not found"),

    METHOD_NOT_ALLOWED("METHOD_NOT_ALLOWED", HttpStatus.METHOD_NOT_ALLOWED, "HTTP method not allowed"),
    NOT_ACCEPTABLE("NOT_ACCEPTABLE", HttpStatus.NOT_ACCEPTABLE, "Requested media type not acceptable"),
    UNSUPPORTED_MEDIA_TYPE("UNSUPPORTED_MEDIA_TYPE", HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported media type"),

    CONFLICT("CONFLICT", HttpStatus.CONFLICT, "Resource conflict"),
    DUPLICATE_RESOURCE("DUPLICATE_RESOURCE", HttpStatus.CONFLICT, "Resource already exists"),
    EMAIL_ALREADY_EXISTS("EMAIL_ALREADY_EXISTS", HttpStatus.CONFLICT, "Email already registered"),
    USERNAME_ALREADY_EXISTS("USERNAME_ALREADY_EXISTS", HttpStatus.CONFLICT, "Username already taken"),

    TOO_MANY_REQUESTS("TOO_MANY_REQUESTS", HttpStatus.TOO_MANY_REQUESTS, "Too many requests, please slow down"),

    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong"),
    DATABASE_ERROR("DATABASE_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "Database error occurred"),
    EXTERNAL_SERVICE_ERROR("EXTERNAL_SERVICE_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "External service failed"),

    SERVICE_UNAVAILABLE("SERVICE_UNAVAILABLE", HttpStatus.SERVICE_UNAVAILABLE, "Service temporarily unavailable"),

    CONFIRM_PASSWORD_NOT_MATCH("CONFIRM_PASSWORD_NOT_MATCH", HttpStatus.BAD_REQUEST, "Confirm password not match");

    private final String code;
    private final HttpStatus status;
    private final String defaultMessage;
}