package com.ducpham.exception;

public class ExceptionHandler extends RuntimeException {
    public ExceptionHandler(EnumException enumException, String message) {
        super(enumException.getMessage() + " " + message);
    }
}
