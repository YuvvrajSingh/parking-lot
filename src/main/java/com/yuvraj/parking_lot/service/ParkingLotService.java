package com.yuvraj.parking_lot.service;

import com.yuvraj.parking_lot.dto.ParkingLotRequest;
import com.yuvraj.parking_lot.dto.ParkingLotResponse;

import java.util.List;

public interface ParkingLotService {

    ParkingLotResponse createParkingLot(ParkingLotRequest request);

    ParkingLotResponse getParkingLotById(Long id);

    List<ParkingLotResponse> getAllParkingLots();
}
