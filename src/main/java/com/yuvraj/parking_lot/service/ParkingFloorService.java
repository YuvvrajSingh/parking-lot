package com.yuvraj.parking_lot.service;

import com.yuvraj.parking_lot.dto.ParkingFloorRequest;
import com.yuvraj.parking_lot.dto.ParkingFloorResponse;

import java.util.List;

public interface ParkingFloorService {

    ParkingFloorResponse addFloor(ParkingFloorRequest request);

    ParkingFloorResponse getFloorById(Long id);

    List<ParkingFloorResponse> getFloorsByParkingLotId(Long parkingLotId);
}
