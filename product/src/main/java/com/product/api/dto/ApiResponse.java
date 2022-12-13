package com.product.api.dto;

import com.product.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SuppressWarnings("unused")
public class ApiResponse<T> extends ResponseEntity<Object> {

    private ApiResponse(HttpStatus status, Object body) {
        super(body, status);
    }

    private static <T> ApiResponse<T> buildResponse(HttpStatus status, T content, String message) {
        Object body;
        if (content != null) {
            body = content;
        } else {
            body = message;
        }

        return new ApiResponse<>(status, body);
    }

    public static <T> ApiResponse<T> statusCreated(String message) {
        return buildResponse(HttpStatus.CREATED, null, message);
    }

    public static <T> ApiResponse<T> statusOk(T content) {
        return buildResponse(HttpStatus.OK, content, null);
    }

    public static ApiException statusBadRequest(String message) {
        return new ApiException(HttpStatus.BAD_REQUEST, message);
    }

    public static ApiException statusNotFound(String message) {
        return new ApiException(HttpStatus.NOT_FOUND, message);
    }

}
