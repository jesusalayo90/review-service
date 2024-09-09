package com.mservices.review.controller;

import com.mservices.review.exception.ErrorResponse;
import com.mservices.review.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

public abstract class BaseController {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handleServiceException(ServiceException e) {
        ErrorResponse error = ErrorResponse.builder()
                .code(e.getErrorCode())
                .message(e.getMessage())
                .time(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
