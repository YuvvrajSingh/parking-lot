package com.yuvraj.parking_lot.exception;

public class VehicleAlreadyParkedException extends RuntimeException {

    public VehicleAlreadyParkedException(String message) {
        super(message);
    }
}
