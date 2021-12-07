package com.example.springbooklibrary.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Error> customException(CustomException exception) {
        return new ResponseEntity<Error>(new Error(exception.getStatus(), exception.getReason()), exception.getStatus());
    }
}