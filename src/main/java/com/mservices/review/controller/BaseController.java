package com.mservices.review.controller;

import com.mservices.review.exception.ErrorResponse;
import com.mservices.review.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.mservices.review.util.ErrorConstants.*;

public abstract class BaseController {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handleServiceException(ServiceException e) {
        ErrorResponse error = ErrorResponse.builder()
                .code(e.getErrorCode())
                .message(e.getMessage())
                .time(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        Class<?> type = e.getRequiredType();
        String propertyName = e.getPropertyName();
        String msg = null, errorCode = null;
        if (type.isEnum()) {
            errorCode = RVW0001;
            msg = Arrays.stream(type.getEnumConstants()).map(c -> c.toString()).collect(Collectors.joining(","));
        } else {
            errorCode = RVW0002;
            msg = type.getTypeName();
        }
        msg = messageSource.getMessage(errorCode, new String[] {propertyName, msg}, LocaleContextHolder.getLocale());

        ErrorResponse error = ErrorResponse.builder()
                .code(errorCode)
                .message(msg)
                .time(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
