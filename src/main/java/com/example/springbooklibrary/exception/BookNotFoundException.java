package com.example.springbooklibrary.exception;

import org.springframework.http.HttpStatus;

public class BookNotFoundException extends CustomException {
    public BookNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Book not found");
    }
}