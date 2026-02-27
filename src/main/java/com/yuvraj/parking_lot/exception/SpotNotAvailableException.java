package com.yuvraj.parking_lot.exception;

public class SpotNotAvailableException extends RuntimeException {

    public SpotNotAvailableException(String message) {
        super(message);
    }
}
