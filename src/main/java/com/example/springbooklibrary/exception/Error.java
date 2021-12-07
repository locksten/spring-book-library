package com.example.springbooklibrary.exception;

import org.springframework.http.HttpStatus;

public class Error {
    private final int status;
    private final String error;
    private final String reason;

    public Error(HttpStatus status, String reason) {
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.reason = reason;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getReason() {
        return reason;
    }
}
