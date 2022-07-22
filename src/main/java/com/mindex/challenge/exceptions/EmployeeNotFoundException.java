package com.mindex.challenge.exceptions;

public class EmployeeNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String message;

    public EmployeeNotFoundException() {
    }

    public EmployeeNotFoundException(String id) {
        super();
        this.message = "Couldn't find employee with id " + id;
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