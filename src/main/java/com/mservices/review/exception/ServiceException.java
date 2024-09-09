package com.mservices.review.exception;

public class ServiceException extends Exception {

    private final String errorCode;

    public ServiceException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
