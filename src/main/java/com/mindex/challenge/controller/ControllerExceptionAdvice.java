package com.mindex.challenge.controller;

import com.mindex.challenge.exceptions.DirectReportEmployeeNotFoundException;
import com.mindex.challenge.exceptions.DuplicateCompensationException;
import com.mindex.challenge.exceptions.EmployeeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<StandardError> handleEmployeeNotFound(EmployeeNotFoundException ex, HttpServletRequest request) {
        String errorMsg = ex.getMessage();
        HttpStatus status = HttpStatus.NOT_FOUND;
        return getResponseError(errorMsg, status, request);
    }

    @ExceptionHandler(DirectReportEmployeeNotFoundException.class)
    public ResponseEntity<StandardError> handleDirectReportEmployeeNotFound(DirectReportEmployeeNotFoundException ex, HttpServletRequest request) {
        String errorMsg = ex.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return getResponseError(errorMsg, status, request);
    }

    @ExceptionHandler(DuplicateCompensationException.class)
    public ResponseEntity<StandardError> handleDuplicateCompensationException(DuplicateCompensationException ex, HttpServletRequest request) {
        String errorMsg = ex.getMessage();
        HttpStatus status = HttpStatus.CONFLICT;
        return getResponseError(errorMsg, status, request);
    }

    private ResponseEntity<StandardError> getResponseError(String message, HttpStatus status, HttpServletRequest request) {
        StandardError error = new StandardError(Instant.now(), status.value(), message, request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }

}