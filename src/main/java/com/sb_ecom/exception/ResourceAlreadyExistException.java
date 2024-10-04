package com.sb_ecom.exception;

public class ResourceAlreadyExistException extends RuntimeException {
    public ResourceAlreadyExistException(String message) {
        super(message);
    }
}
