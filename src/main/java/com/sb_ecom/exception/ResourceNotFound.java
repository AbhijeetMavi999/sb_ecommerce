package com.sb_ecom.exception;

public class ResourceNotFound extends Exception {
    public ResourceNotFound() {
        super();
    }

    public ResourceNotFound(String message) {
        super(message);
    }

    public ResourceNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    protected ResourceNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ResourceNotFound(Throwable cause) {
        super(cause);
    }
}
