package com.example.springbooklibrary.exception;

import org.springframework.http.HttpStatus;

public class CheckoutEndBeforeStartException extends CustomException {
    public CheckoutEndBeforeStartException() {
        super(HttpStatus.BAD_REQUEST, "End of checkout cannot be before it's start.");
    }
}