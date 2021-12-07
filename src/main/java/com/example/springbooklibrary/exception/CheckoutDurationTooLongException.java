package com.example.springbooklibrary.exception;

import org.springframework.http.HttpStatus;

import java.time.Duration;

public class CheckoutDurationTooLongException extends CustomException {
    public CheckoutDurationTooLongException(Duration duration) {
        super(HttpStatus.BAD_REQUEST, String.format("Checking out a book for longer than %d days is not allowed.", duration.toDays()));
    }
}