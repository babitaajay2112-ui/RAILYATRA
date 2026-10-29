package com.railway.trainproject.exception;

public class InsufficientSeatsException extends RuntimeException {

    public InsufficientSeatsException(String message) {
        super(message);
    }
}
