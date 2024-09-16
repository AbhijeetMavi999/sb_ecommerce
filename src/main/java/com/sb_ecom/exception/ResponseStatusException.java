package com.sb_ecom.exception;

public class ResponseStatusException extends Exception {
    public ResponseStatusException() {
        super();
    }

    public ResponseStatusException(String message) {
        super(message);
    }

    public ResponseStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    protected ResponseStatusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ResponseStatusException(Throwable cause) {
        super(cause);
    }
}
