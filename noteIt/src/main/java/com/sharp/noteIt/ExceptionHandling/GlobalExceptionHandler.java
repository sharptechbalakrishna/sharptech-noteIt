package com.sharp.noteIt.ExceptionHandling;

import java.util.NoSuchElementException;

import javax.security.auth.login.AccountNotFoundException;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body("Invalid credentials provided");
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFoundException(AccountNotFoundException e) {
        return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
    }
}
