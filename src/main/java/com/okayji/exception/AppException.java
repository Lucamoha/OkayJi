package com.okayji.exception;

public class AppException extends RuntimeException {
    public AppException(AppError errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    private AppError errorCode;

    public AppError getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(AppError errorCode) {
        this.errorCode = errorCode;
    }
}
