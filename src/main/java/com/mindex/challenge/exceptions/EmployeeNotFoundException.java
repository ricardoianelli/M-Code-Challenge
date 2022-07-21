package com.mindex.challenge.exceptions;

public class EmployeeNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String message;

    public EmployeeNotFoundException() {
    }

    public EmployeeNotFoundException(String message) {
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