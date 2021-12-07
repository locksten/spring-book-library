package com.example.springbooklibrary.exception;

import org.springframework.http.HttpStatus;

public class TooManyCheckoutsException extends CustomException {
    public TooManyCheckoutsException(int threshold) {
        super(HttpStatus.BAD_REQUEST, String.format("Cannot have more than %d books checked out simultaneously.", threshold));
    }
}