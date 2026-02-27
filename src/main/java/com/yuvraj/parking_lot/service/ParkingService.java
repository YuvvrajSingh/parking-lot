package com.yuvraj.parking_lot.service;

import com.yuvraj.parking_lot.dto.AvailabilityResponse;
import com.yuvraj.parking_lot.dto.ParkRequest;
import com.yuvraj.parking_lot.dto.VehicleResponse;

public interface ParkingService {

    VehicleResponse parkVehicle(ParkRequest request);

    VehicleResponse exitVehicle(String licensePlate);

    AvailabilityResponse getAvailableSpots();
}
