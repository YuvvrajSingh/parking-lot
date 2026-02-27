package com.yuvraj.parking_lot.service;

import com.yuvraj.parking_lot.dto.ParkingSpotRequest;
import com.yuvraj.parking_lot.dto.ParkingSpotResponse;
import com.yuvraj.parking_lot.entity.enums.ParkingSpotType;

import java.util.List;

public interface ParkingSpotService {

    ParkingSpotResponse addSpot(ParkingSpotRequest request);

    ParkingSpotResponse getSpotById(Long id);

    List<ParkingSpotResponse> getAvailableSpotsByType(ParkingSpotType type);

    List<ParkingSpotResponse> getSpotsByFloorId(Long floorId);
}
