package com.mindex.challenge.exceptions;

public class DuplicateCompensationException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String message;

    public DuplicateCompensationException() {
    }

    public DuplicateCompensationException(String message) {
        super();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static long getSerialVersionUId() {
        return serialVersionUID;
    }
}