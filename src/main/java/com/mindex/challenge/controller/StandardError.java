package com.mindex.challenge.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.time.Instant;

public class StandardError implements Serializable {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private Instant timestamp;
    private Integer statusCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    private String path;

    public StandardError() {
    }

    public StandardError(Instant timestamp, Integer statusCode, String message, String path) {
        this.timestamp = timestamp;
        this.statusCode = statusCode;
        this.message = message;
        this.path = path;

        if (message != null && message.isEmpty()) {
            this.message = null;
        }
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}