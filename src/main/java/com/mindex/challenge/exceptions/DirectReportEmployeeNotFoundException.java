package com.mindex.challenge.exceptions;

public class DirectReportEmployeeNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String message;

    public DirectReportEmployeeNotFoundException() {
    }

    public DirectReportEmployeeNotFoundException(String id) {
        super();
        this.message = "Couldn't find direct report employee with id " + id;
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